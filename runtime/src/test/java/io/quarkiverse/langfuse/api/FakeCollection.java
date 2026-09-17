package io.quarkiverse.langfuse.api;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import io.quarkiverse.langfuse.api.cursor.Cursor;
import io.quarkiverse.langfuse.api.cursor.CursorResult;
import io.quarkiverse.langfuse.api.paging.Page;
import io.quarkiverse.langfuse.api.paging.PagedResult;
import io.smallrye.mutiny.Uni;

/**
 * An in-memory stand-in for a paginated Langfuse collection, used to verify the traversal engines
 * without any HTTP involved.
 *
 * <p>
 * Records every request it serves, so tests can assert not just <em>what</em> came back but
 * <em>how many</em> requests it took - which is how laziness and short-circuiting are proven. The
 * same mechanism is what proves a delete unit issues <em>no</em> request for a name it could not
 * resolve.
 *
 * <p>
 * Overlap between concurrent deletes is proven by a <em>rendezvous</em> rather than by sleeping:
 * see {@link #withRendezvous(int)}. Sleeping only makes overlap likely, so the lower bound on
 * {@link #peakInFlight()} would be a timing guess; a rendezvous makes it a fact.
 */
final class FakeCollection {
    private static final Duration RENDEZVOUS_TIMEOUT = Duration.ofSeconds(5);

    private final List<String> items;
    private final AtomicInteger requests = new AtomicInteger();
    private final List<String> deleted = new CopyOnWriteArrayList<>();
    private final AtomicInteger inFlight = new AtomicInteger();
    private final AtomicInteger peakInFlight = new AtomicInteger();
    private final List<String> deletingThreads = new CopyOnWriteArrayList<>();
    private final AtomicInteger arrivals = new AtomicInteger();
    private volatile CyclicBarrier rendezvous;
    private volatile int rendezvousParties;

    private FakeCollection(List<String> items) {
        this.items = items;
    }

    static FakeCollection of(int itemCount) {
        return new FakeCollection(IntStream.rangeClosed(1, itemCount)
                .mapToObj("item-%d"::formatted)
                .toList());
    }

    /**
     * Makes {@link #deleteTracked(String)} hold the first {@code parties} deletes open until all of
     * them have arrived, so {@link #peakInFlight()} observes genuine overlap rather than a hopeful
     * one.
     *
     * <p>
     * {@code parties} is the concurrency the implementation is <em>expected to achieve</em>, which is
     * not always the configured ceiling: it is capped by the batch size, and the sync fan-out counts
     * the calling thread as one of its runners. Passing more parties than the implementation can
     * deliver makes the barrier time out - which is the intended failure signal, not a hang.
     *
     * <p>
     * Only the first {@code parties} arrivals wait. Every later delete passes straight through,
     * because a barrier that tripped every {@code parties} arrivals would deadlock whenever the batch
     * size is not an exact multiple of {@code parties} - the remainder would wait forever for peers
     * that do not exist.
     *
     * @param parties the number of deletes that must overlap before any of them may proceed
     * @return this collection, for chaining onto {@link #of(int)}
     */
    FakeCollection withRendezvous(int parties) {
        this.rendezvousParties = parties;
        this.rendezvous = new CyclicBarrier(parties);

        return this;
    }

    int requestCount() {
        return this.requests.get();
    }

    int totalItems() {
        return this.items.size();
    }

    // --- delete support --------------------------------------------------------------------

    /**
     * The ids passed to {@link #delete(String)}, in call order. Empty proves no delete was issued.
     */
    List<String> deletedIds() {
        return List.copyOf(this.deleted);
    }

    /**
     * Resolves a name the collection knows about, counting the lookup as a request.
     */
    Optional<String> resolve(String name) {
        this.requests.incrementAndGet();

        return this.items.stream()
                .filter(name::equals)
                .findFirst()
                .map("id-of-%s"::formatted);
    }

    /**
     * The asynchronous resolve contract: emits {@code null} rather than an empty {@link Optional} when
     * nothing matched, mirroring {@code AbstractAsyncPagedOperations.scanForName}.
     */
    Uni<String> resolveAsync(String name) {
        return Uni.createFrom().item(() -> resolve(name).orElse(null));
    }

    void delete(String id) {
        this.requests.incrementAndGet();
        this.deleted.add(id);
    }

    /**
     * The highest number of deletes observed running at the same time.
     */
    int peakInFlight() {
        return this.peakInFlight.get();
    }

    /**
     * The distinct thread names that ran a delete, used only to prove no work was submitted.
     */
    List<String> deletingThreadNames() {
        return this.deletingThreads.stream()
                .distinct()
                .toList();
    }

    /**
     * Deletes while recording peak overlap, rendezvousing first when {@link #withRendezvous(int)} has
     * been configured so the overlap is proven rather than hoped for.
     */
    void deleteTracked(String id) {
        this.deletingThreads.add(Thread.currentThread().getName());
        this.peakInFlight.accumulateAndGet(this.inFlight.incrementAndGet(), Math::max);

        try {
            // The in-flight count is raised BEFORE waiting, so every party is already counted by the
            // time the last one arrives and releases them all.
            awaitRendezvous();
            delete(id);
        } finally {
            this.inFlight.decrementAndGet();
        }
    }

    // Only the first `rendezvousParties` arrivals wait; the rest pass through. See withRendezvous.
    private void awaitRendezvous() {
        var barrier = this.rendezvous;

        if ((barrier != null) && (this.arrivals.getAndIncrement() < this.rendezvousParties)) {
            try {
                barrier.await(RENDEZVOUS_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                throw new IllegalStateException("Interrupted while awaiting the delete rendezvous", e);
            } catch (TimeoutException | BrokenBarrierException e) {
                throw new AssertionError(
                        "Only %d of %d deletes ran concurrently within %s - the implementation never reached the expected concurrency"
                                .formatted(this.inFlight.get(), this.rendezvousParties, RENDEZVOUS_TIMEOUT),
                        e);
            }
        }
    }

    Uni<String> deleteTrackedAsync(String id) {
        return Uni.createFrom()
                .item(() -> {
                    this.peakInFlight.accumulateAndGet(this.inFlight.incrementAndGet(), Math::max);

                    return id;
                })
                .onItem().delayIt().by(Duration.ofMillis(20))
                .invoke(() -> delete(id))
                // eventually, not invoke: a failed delete must still release the in-flight count, or a
                // leaked counter silently inflates peakInFlight and the assertion passes for the wrong
                // reason.
                .eventually(this.inFlight::decrementAndGet);
    }

    Uni<String> deleteAsync(String id) {
        return Uni.createFrom().item(() -> {
            delete(id);

            return id;
        });
    }

    // Delays each delete in proportion to how early its identifier appears, so the units complete in
    // reverse input order. Any result that still reads in input order got there by position rather than
    // by completion.
    Function<String, Uni<?>> deleteAsyncDelayedInReverse(List<String> ordered) {
        return id -> deleteAsync(id)
                .onItem().delayIt().by(Duration.ofMillis(10L * (ordered.size() - ordered.indexOf(id))));
    }

    PagedResult<String> page(Page page) {
        this.requests.incrementAndGet();

        var from = Math.min((page.index() - 1) * page.size(), this.items.size());
        var to = Math.min(from + page.size(), this.items.size());

        return PagedResult.of(this.items.subList(from, to), page, this.items.size(), totalPages(page.size()));
    }

    CursorResult<String> batch(Cursor cursor) {
        this.requests.incrementAndGet();

        var from = cursor.value()
                .map(Integer::parseInt)
                .orElse(0);
        var to = Math.min(from + cursor.limit(), this.items.size());
        var nextCursor = (to < this.items.size()) ? String.valueOf(to) : null;

        return CursorResult.of(this.items.subList(Math.min(from, this.items.size()), to), cursor, nextCursor);
    }

    Uni<PagedResult<String>> pageAsync(Page page) {
        return Uni.createFrom().item(() -> page(page));
    }

    Uni<CursorResult<String>> batchAsync(Cursor cursor) {
        return Uni.createFrom().item(() -> batch(cursor));
    }

    private int totalPages(int pageSize) {
        return (this.items.size() + pageSize - 1) / pageSize;
    }
}
