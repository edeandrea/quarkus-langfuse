package io.quarkiverse.langfuse.deployment.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
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
 * Serves a fake, page-addressed Langfuse collection so the higher-level operations can be exercised
 * over the real REST client, exception mapper and Jackson stack.
 *
 * <p>
 * Stubs are registered per page, which is what lets tests assert not only the items returned but also
 * exactly which pages were requested - the only honest way to verify laziness and short-circuiting.
 *
 * <p>
 * Subclasses supply the two things that genuinely differ per domain: the collection path and the JSON
 * for a single item. The pagination envelope and the page iteration are identical everywhere, so they
 * live here.
 */
abstract class PagedCollectionTestSupport extends WiremockAware {
    private final String collectionPath;
    private final IntFunction<String> itemJson;

    /**
     * @param collectionPath the path this collection is served from, e.g. {@code /api/public/models}
     * @param itemJson renders a single item's JSON given its 1-based ordinal
     */
    protected PagedCollectionTestSupport(String collectionPath, IntFunction<String> itemJson) {
        this.collectionPath = collectionPath;
        this.itemJson = itemJson;
    }

    /**
     * Registers {@code itemCount} items spread over pages of {@code pageSize}, each page stubbed
     * against its own {@code page}/{@code limit} query parameters.
     */
    void stubCollection(int itemCount, int pageSize) {
        var totalPages = Math.max(1, (itemCount + pageSize - 1) / pageSize);

        IntStream.rangeClosed(1, totalPages)
                .forEach(page -> stubPage(page, pageSize, itemCount, totalPages));
    }

    private void stubPage(int page, int pageSize, int itemCount, int totalPages) {
        var from = (page - 1) * pageSize;
        var to = Math.min(from + pageSize, itemCount);

        var items = IntStream.range(from, to)
                .mapToObj(index -> this.itemJson.apply(index + 1))
                .toList();

        wiremock().register(
                get(urlPathEqualTo(this.collectionPath))
                        .withQueryParam("page", equalTo(String.valueOf(page)))
                        .withQueryParam("limit", equalTo(String.valueOf(pageSize)))
                        .willReturn(okJson("""
                                {
                                  "data": [%s],
                                  "meta": {
                                    "page": %d,
                                    "limit": %d,
                                    "totalItems": %d,
                                    "totalPages": %d
                                  }
                                }
                                """.formatted(String.join(",", items), page, pageSize, itemCount, totalPages))));
    }

    void verifyListRequests(int times) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(this.collectionPath)));
    }

    void verifyPageRequested(int times, int page) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(this.collectionPath))
                .withQueryParam("page", equalTo(String.valueOf(page))));
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
