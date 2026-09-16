package io.quarkiverse.langfuse.deployment.api;

import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.util.stream.IntStream;

import io.quarkiverse.langfuse.deployment.WiremockAware;

/**
 * Serves a fake {@code /api/public/v2/evaluators} cursor-addressed collection.
 */
abstract class EvaluatorOperationsTestSupport extends WiremockAware {
    static final String EVALUATORS_PATH = "/api/public/v2/evaluators";

    void stubEvaluators(int itemCount, int batchSize) {
        var totalBatches = Math.max(1, (itemCount + batchSize - 1) / batchSize);

        IntStream.rangeClosed(1, totalBatches)
                .forEach(batch -> stubBatch(batch, batchSize, itemCount, totalBatches));
    }

    private void stubBatch(int batch, int batchSize, int itemCount, int totalBatches) {
        var from = (batch - 1) * batchSize;
        var to = Math.min(from + batchSize, itemCount);

        var evaluators = IntStream.range(from, to)
                .mapToObj(index -> """
                        {
                          \"id\": \"evaluator-%1$d\",
                          \"name\": \"evaluator-%1$d\",
                          \"projectId\": \"project-1\",
                          \"type\": \"code\",
                          \"sourceCode\": \"return 1\",
                          \"sourceCodeLanguage\": \"PYTHON\"
                        }""".formatted(index + 1))
                .toList();

        // The cursor requested to GET this batch: empty on batch 1, the previous batch's "to" otherwise
        var requestedCursor = (batch == 1) ? null : String.valueOf(from);

        // The next cursor emitted in this batch's response: the new "to" if more remain, absent on the last
        var nextCursorJson = (batch < totalBatches)
                ? "\"cursor\": \"%d\"".formatted(to)
                : "";

        var mapping = get(urlPathEqualTo(EVALUATORS_PATH))
                .withQueryParam("limit", equalTo(String.valueOf(batchSize)));

        if (requestedCursor == null) {
            mapping = mapping.withQueryParam("cursor", absent());
        } else {
            mapping = mapping.withQueryParam("cursor", equalTo(requestedCursor));
        }

        wiremock().register(mapping.willReturn(okJson("""
                {
                  \"data\": [%s],
                  \"meta\": {
                    %s
                  }
                }
                """.formatted(String.join(",", evaluators), nextCursorJson))));
    }

    void verifyListRequests(int times) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(EVALUATORS_PATH)));
    }

    void verifyInitialBatchRequested(int times) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(EVALUATORS_PATH))
                .withoutQueryParam("cursor"));
    }

    void verifyResumedBatchRequested(int times, String cursor) {
        wiremock().verifyThat(times, getRequestedFor(urlPathEqualTo(EVALUATORS_PATH))
                .withQueryParam("cursor", equalTo(cursor)));
    }

    void verifyEvaluatorsCreated(int times) {
        wiremock().verifyThat(times, postRequestedFor(urlPathEqualTo(EVALUATORS_PATH)));
    }
}
