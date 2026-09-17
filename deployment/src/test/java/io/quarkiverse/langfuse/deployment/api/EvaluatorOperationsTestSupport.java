package io.quarkiverse.langfuse.deployment.api;

/**
 * Serves a fake {@code /api/public/v2/evaluators} cursor-addressed collection.
 */
abstract class EvaluatorOperationsTestSupport extends CursorCollectionTestSupport {
    static final String EVALUATORS_PATH = "/api/public/v2/evaluators";

    protected EvaluatorOperationsTestSupport() {
        super(EVALUATORS_PATH, ordinal -> """
                {
                  "id": "evaluator-%1$d",
                  "name": "evaluator-%1$d",
                  "projectId": "project-1",
                  "type": "code",
                  "sourceCode": "return 1",
                  "sourceCodeLanguage": "PYTHON"
                }""".formatted(ordinal));
    }

    void stubEvaluators(int itemCount, int batchSize) {
        stubCollection(itemCount, batchSize);
    }

    void verifyEvaluatorsCreated(int times) {
        verifyItemsCreated(times);
    }
}
