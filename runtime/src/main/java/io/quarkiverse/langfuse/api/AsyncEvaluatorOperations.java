package io.quarkiverse.langfuse.api;

import java.util.Objects;

import com.langfuse.api.model.CreateCodeEvaluatorRequest1;
import com.langfuse.api.model.CreateEvaluatorRequest;
import com.langfuse.api.model.CreateLlmAsJudgeEvaluatorRequest1;
import com.langfuse.api.model.Evaluator;

import io.smallrye.mutiny.Uni;

/**
 * Higher-level operations over Langfuse evaluators, returning Mutiny types.
 *
 * <p>
 * Obtained from {@link AsyncLangfuseOperations#evaluators()}. The asynchronous counterpart of
 * {@link EvaluatorOperations}; the two behave identically apart from how absence is represented,
 * which follows each style's own convention: {@link java.util.Optional} for the synchronous tree, a
 * {@code null} item for the asynchronous one.
 *
 * @see EvaluatorOperations
 */
public sealed interface AsyncEvaluatorOperations extends AsyncCursorOperations<Evaluator>
        permits DefaultAsyncEvaluatorOperations {

    /**
     * Finds an evaluator by its exact name.
     *
     * <p>
     * <strong>Emits {@code null} if no evaluator has that name.</strong>
     *
     * @param evaluatorName the evaluator name to look for, must not be {@code null} or blank
     * @return the matching evaluator, or {@code null} if no evaluator has that name
     * @throws IllegalArgumentException if {@code evaluatorName} is {@code null} or blank
     */
    Uni<Evaluator> findByName(String evaluatorName);

    /**
     * Whether an evaluator with the given exact name exists.
     *
     * @param evaluatorName the evaluator name to look for, must not be {@code null} or blank
     * @return {@code true} if an evaluator with that name exists. Never {@code null}
     * @throws IllegalArgumentException if {@code evaluatorName} is {@code null} or blank
     */
    default Uni<Boolean> exists(String evaluatorName) {
        return findByName(evaluatorName)
                .map(Objects::nonNull);
    }

    /**
     * Returns the evaluator with the requested name, creating it if no evaluator has that name.
     *
     * <p>
     * <strong>Not atomic.</strong> Langfuse offers no create-or-update-by-name operation for evaluators,
     * so this performs a lookup followed by a create. Concurrent callers may therefore both observe the
     * evaluator as absent and both create it.
     *
     * @param request the evaluator to create if it is missing
     * @return the existing or newly created evaluator. Never {@code null}
     */
    Uni<Evaluator> createIfAbsent(CreateEvaluatorRequest request);

    /**
     * Convenience overload of {@link #createIfAbsent(CreateEvaluatorRequest)} for a code evaluator.
     *
     * @param request the code evaluator to create if it is missing
     * @return the existing or newly created evaluator. Never {@code null}
     */
    default Uni<Evaluator> createIfAbsent(CreateCodeEvaluatorRequest1 request) {
        Objects.requireNonNull(request, "request must not be null");
        return createIfAbsent(new CreateEvaluatorRequest(request));
    }

    /**
     * Convenience overload of {@link #createIfAbsent(CreateEvaluatorRequest)} for an LLM-as-a-judge
     * evaluator.
     *
     * @param request the LLM-as-a-judge evaluator to create if it is missing
     * @return the existing or newly created evaluator. Never {@code null}
     */
    default Uni<Evaluator> createIfAbsent(CreateLlmAsJudgeEvaluatorRequest1 request) {
        Objects.requireNonNull(request, "request must not be null");
        return createIfAbsent(new CreateEvaluatorRequest(request));
    }
}
