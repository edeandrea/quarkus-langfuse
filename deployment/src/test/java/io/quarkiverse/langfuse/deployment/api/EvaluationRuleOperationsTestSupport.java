package io.quarkiverse.langfuse.deployment.api;

/**
 * Serves a fake {@code /api/public/v2/evaluation-rules} cursor-addressed collection.
 */
abstract class EvaluationRuleOperationsTestSupport extends CursorCollectionTestSupport {
    static final String RULES_PATH = "/api/public/v2/evaluation-rules";

    protected EvaluationRuleOperationsTestSupport() {
        super(RULES_PATH, ordinal -> """
                {
                  "id": "rule-%1$d",
                  "name": "rule-%1$d",
                  "projectId": "project-1"
                }""".formatted(ordinal));
    }

    void stubRules(int itemCount, int batchSize) {
        stubCollection(itemCount, batchSize);
    }

    void verifyRulesCreated(int times) {
        verifyItemsCreated(times);
    }
}
