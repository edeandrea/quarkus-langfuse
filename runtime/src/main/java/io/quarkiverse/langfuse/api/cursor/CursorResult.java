package io.quarkiverse.langfuse.api.cursor;

import java.util.List;
import java.util.Optional;

import io.quarkiverse.langfuse.api.paging.PagedResult;

/**
 * A single batch of results from a cursor-addressed collection, together with the cursor needed to
 * continue.
 *
 * <p>
 * The cursor-addressed counterpart of {@link PagedResult}. Langfuse's cursor metadata carries only
 * the next cursor, so - unlike {@link PagedResult} - no totals are available. Nothing here is
 * inferred or fabricated from what the server does not report.
 *
 * <pre>{@code
 * var batch = langfuse.evaluationRules().findBatch(Cursor.first(75));
 *
 * batch.nextCursor()
 *     .map(langfuse.evaluationRules()::findBatch)
 *     .ifPresent(next -> ...);
 * }</pre>
 *
 * @param <T> the type of the items in the batch
 * @see Cursor
 * @see CursorSelection
 */
public sealed interface CursorResult<T> permits DefaultCursorResult {

    /**
     * Creates a batch of results.
     *
     * <p>
     * Intended for callers that already hold the items and the next cursor value - typically a test
     * double or an adapter over a source other than the generated client. The operations in this layer
     * produce their own results, so applications rarely need this.
     *
     * <p>
     * The arguments are normalized rather than rejected, so a result is always well-formed: a
     * {@code null} {@code items} becomes an empty list, and the list is copied so later changes to it
     * are not visible here. A {@code null} or blank {@code nextCursorValue} both mean "no further
     * data", so {@link #nextCursor()} is empty for either.
     *
     * @param <T> the type of the items in the batch
     * @param items the items in this batch, may be {@code null} or empty
     * @param cursor the cursor these items were requested with, must not be {@code null}
     * @param nextCursorValue the opaque cursor value for the next batch, or {@code null} when Langfuse
     *        reports no further data
     * @return the batch of results
     * @throws IllegalArgumentException if {@code cursor} is {@code null}
     */
    static <T> CursorResult<T> of(List<T> items, Cursor cursor, String nextCursorValue) {
        return new DefaultCursorResult<>(items, cursor, nextCursorValue);
    }

    /**
     * The items in this batch, in the order returned by Langfuse.
     *
     * <p>
     * Never {@code null}. Empty when there is no further data.
     *
     * @return an unmodifiable list of the items in this batch
     */
    List<T> items();

    /**
     * The cursor these items were requested with.
     *
     * @return the cursor
     */
    Cursor cursor();

    /**
     * The cursor for the next batch, or empty when Langfuse reports no further data.
     *
     * <p>
     * The returned cursor keeps the same limit as {@link #cursor()}.
     *
     * @return the next cursor, if there is one
     */
    Optional<Cursor> nextCursor();

    /**
     * Whether a further batch is available.
     *
     * @return {@code true} if {@link #nextCursor()} is present
     */
    default boolean hasNext() {
        return nextCursor().isPresent();
    }
}
