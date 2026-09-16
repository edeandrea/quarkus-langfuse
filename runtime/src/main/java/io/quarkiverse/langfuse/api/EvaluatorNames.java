package io.quarkiverse.langfuse.api;

import java.util.Objects;
import java.util.Optional;

import com.langfuse.api.model.CodeEvaluator1;
import com.langfuse.api.model.CreateCodeEvaluatorRequest1;
import com.langfuse.api.model.CreateEvaluatorRequest;
import com.langfuse.api.model.CreateLlmAsJudgeEvaluatorRequest1;
import com.langfuse.api.model.Evaluator;
import com.langfuse.api.model.LlmAsJudgeEvaluator1;

/**
 * Extracts natural-key names from polymorphic evaluator models.
 */
final class EvaluatorNames {

    private EvaluatorNames() {
    }

    /**
     * Extracts the evaluator name from an {@link Evaluator}.
     *
     * @param evaluator the evaluator, may be {@code null}
     * @return the evaluator name, or {@code null} if absent or unrecognized
     */
    static String of(Evaluator evaluator) {
        return Optional.ofNullable(evaluator)
                .map(Evaluator::getActualInstance)
                .flatMap(instance -> Optional.of(instance)
                        .filter(CodeEvaluator1.class::isInstance)
                        .map(CodeEvaluator1.class::cast)
                        .map(CodeEvaluator1::getName)
                        .or(() -> Optional.of(instance)
                                .filter(LlmAsJudgeEvaluator1.class::isInstance)
                                .map(LlmAsJudgeEvaluator1.class::cast)
                                .map(LlmAsJudgeEvaluator1::getName)))
                .orElse(null);
    }

    /**
     * Extracts the evaluator name from a {@link CreateEvaluatorRequest}.
     *
     * @param request the create request, must not be {@code null}
     * @return the evaluator name, or {@code null} if unrecognized
     * @throws NullPointerException if {@code request} is {@code null}
     */
    static String of(CreateEvaluatorRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        return Optional.ofNullable(request.getActualInstance())
                .flatMap(instance -> Optional.of(instance)
                        .filter(CreateCodeEvaluatorRequest1.class::isInstance)
                        .map(CreateCodeEvaluatorRequest1.class::cast)
                        .map(CreateCodeEvaluatorRequest1::getName)
                        .or(() -> Optional.of(instance)
                                .filter(CreateLlmAsJudgeEvaluatorRequest1.class::isInstance)
                                .map(CreateLlmAsJudgeEvaluatorRequest1.class::cast)
                                .map(CreateLlmAsJudgeEvaluatorRequest1::getName)))
                .orElse(null);
    }
}
