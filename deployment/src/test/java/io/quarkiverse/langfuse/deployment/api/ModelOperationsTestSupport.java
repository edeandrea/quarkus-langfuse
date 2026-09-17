package io.quarkiverse.langfuse.deployment.api;

import com.github.tomakehurst.wiremock.client.WireMock;

/**
 * Serves a fake, paginated {@code /api/public/models} collection.
 */
abstract class ModelOperationsTestSupport extends PagedCollectionTestSupport {
    static final String MODELS_PATH = "/api/public/models";

    protected ModelOperationsTestSupport() {
        super(MODELS_PATH, ordinal -> """
                {
                  "id": "model-%1$d",
                  "modelName": "model-%1$d",
                  "isLangfuseManaged": false
                }""".formatted(ordinal));
    }

    void stubModels(int itemCount, int pageSize) {
        stubCollection(itemCount, pageSize);
    }

    void verifyModelsCreated(int times) {
        verifyItemsCreated(times);
    }

    WireMock resetAndGetWiremock() {
        resetMappings();
        resetRequests();

        return wiremock();
    }
}
