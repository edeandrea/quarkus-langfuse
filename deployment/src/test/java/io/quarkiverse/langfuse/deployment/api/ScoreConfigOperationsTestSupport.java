package io.quarkiverse.langfuse.deployment.api;

/**
 * Serves a fake {@code /api/public/score-configs} collection.
 */
abstract class ScoreConfigOperationsTestSupport extends PagedCollectionTestSupport {
    static final String SCORE_CONFIGS_PATH = "/api/public/score-configs";

    protected ScoreConfigOperationsTestSupport() {
        super(SCORE_CONFIGS_PATH, ordinal -> """
                {
                  "id": "config-%1$d",
                  "name": "config-%1$d",\
                  "dataType": "NUMERIC",
                  "projectId": "project-1"
                }""".formatted(ordinal));
    }

    void stubConfigs(int itemCount, int pageSize) {
        stubCollection(itemCount, pageSize);
    }

    void verifyConfigsCreated(int times) {
        verifyItemsCreated(times);
    }
}
