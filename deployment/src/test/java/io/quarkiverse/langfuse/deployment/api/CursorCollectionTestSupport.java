package io.quarkiverse.langfuse.deployment.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

import io.quarkiverse.langfuse.deployment.WiremockAware;

/**
 * Serves a fake, cursor-addressed Langfuse collection, whose batches are reached with an opaque
 * server-issued cursor rather than a page index.
 *
 * <p>
 * The cursor-addressed counterpart of {@link PagedCollectionTestSupport}. Stubs are registered per
 * batch and matched on the exact cursor that should have been sent to reach them, so a test proves
 * not only what came back but that the traversal resumed from the right place.
 *
 * <p>
 * Subclasses supply the two things that genuinely differ per domain: the collection path and the JSON
 * for a single item.
 */
abstract class CursorCollectionTestSupport extends WiremockAware {
    private final String collectionPath;
    private final IntFunction<String> itemJson;

    /**
     * @param collectionPath the path this collection is served from, e.g. {@code /api/public/v2/evaluators}
     * @param itemJson renders a single item's JSON given its 1-based ordinal
     */
    protected CursorCollectionTestSupport(String collectionPath, IntFunction<String> itemJson) {
        this.collectionPath = collectionPath;
        this.itemJson = itemJson;
    }

    /**
     * Registers {@code itemCount} items spread over batches of {@code batchSize}, each batch stubbed
     * against the cursor that reaches it.
     */
    void stubCollection(int itemCount, int batchSize) {
        var totalBatches = Math.max(1, (itemCount + batchSize - 1) / batchSize);

        IntStream.rangeClosed(1, totalBatches)
                .forEach(batch -> stubBatch(batch, batchSize, itemCount, totalBatches));
    }

    private void stubBatch(int batch, int batchSize, int itemCount, int totalBatches) {
        var from = (batch - 1) * batchSize;
        var to = Math.min(from + batchSize, itemCount);

        var items = IntStream.range(from, to)
                .mapToObj(index -> this.itemJson.apply(index + 1))
                .toList();

        // The cursor requested to GET this batch: empty on batch 1, the previous batch's "to" otherwise
        var requestedCursor = (batch == 1) ? null : String.valueOf(from);

        // The next cursor emitted in this batch's response: the new "to" if more remain, absent on the last
        var nextCursorJson = (batch < totalBatches)
                ? "\"cursor\": \"%d\"".formatted(to)
                : "";

        var mapping = get(urlPathEqualTo(this.collectionPath))
                .withQueryParam("limit", equalTo(String.valueOf(batchSize)));

        if (requestedCursor == null) {
            mapping = mapping.withQueryParam("cursor", absent());
        } else {
            mapping = mapping.withQueryParam("cursor", equalTo(requestedCursor));
        }

        wiremock().register(mapping.willReturn(okJson("""
                {
                  "data": [%s],
                  "meta": {
                    %s
                  }
                }
                """.formatted(String.join(",", items), nextCursorJson))));
    }

    void verifyListRequests(int times) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(this.collectionPath)));
    }

    void verifyInitialBatchRequested(int times) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(this.collectionPath))
                .withoutQueryParam("cursor"));
    }

    void verifyResumedBatchRequested(int times, String cursor) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(this.collectionPath))
                .withQueryParam("cursor", equalTo(cursor)));
    }

    void verifyItemsCreated(int times) {
        wiremock().verifyThat(times, postRequestedFor(urlPathEqualTo(this.collectionPath)));
    }

    /**
     * Makes the listing endpoint fail with {@code 404}.
     */
    void stubListingFailure() {
        stubListingFailure(404);
    }

    /**
     * Makes the listing endpoint fail with the given status, so tests can distinguish absence from
     * failure - a 404 that the operations layer tolerates as "no such collection" reads very
     * differently from a 401 or 500, which must propagate.
     */
    void stubListingFailure(int status) {
        wiremock().register(get(urlPathEqualTo(this.collectionPath))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\":\"not found\"}")));
    }
}
