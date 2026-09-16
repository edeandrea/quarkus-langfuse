package io.quarkiverse.langfuse.api;

import java.util.Objects;
import java.util.Optional;

import com.langfuse.api.model.CreateCodeEvaluatorRequest1;
import com.langfuse.api.model.CreateEvaluatorRequest;
import com.langfuse.api.model.CreateLlmAsJudgeEvaluatorRequest1;
import com.langfuse.api.model.Evaluator;

/**
 * Higher-level operations over Langfuse evaluators.
 *
 * <p>
 * Obtained from {@link LangfuseOperations#evaluators()}. Like evaluation rules, evaluators are a
 * <strong>cursor-addressed</strong> collection - Langfuse reports only an opaque next cursor, not a
 * page index or totals - so {@link #stream}, {@link #streamBatches} and {@link #findBatch} - inherited
 * from {@link CursorOperations} - accept a {@link CursorSelection} or a {@link Cursor} rather than
 * their page-addressed equivalents.
 *
 * @see AsyncEvaluatorOperations
 */
public sealed interface EvaluatorOperations extends CursorOperations<Evaluator>
        permits DefaultEvaluatorOperations {

    /**
     * Finds an evaluator by its exact name.
     *
     * <p>
     * Langfuse offers no name filter for evaluators, so this walks the collection and stops at the first
     * match: an evaluator found in the first batch costs a single request.
     *
     * @param evaluatorName the evaluator name to look for, must not be {@code null} or blank
     * @return the matching evaluator, or empty if no evaluator has that name
     * @throws IllegalArgumentException if {@code evaluatorName} is {@code null} or blank
     * @throws com.langfuse.api.LangfuseApiException if the request fails for any reason other than the
     *         evaluator not existing
     */
    Optional<Evaluator> findByName(String evaluatorName);

    /**
     * Whether an evaluator with the given exact name exists.
     *
     * @param evaluatorName the evaluator name to look for, must not be {@code null} or blank
     * @return {@code true} if an evaluator with that name exists
     * @throws IllegalArgumentException if {@code evaluatorName} is {@code null} or blank
     * @throws com.langfuse.api.LangfuseApiException if the request fails for any reason other than the
     *         evaluator not existing
     */
    default boolean exists(String evaluatorName) {
        return findByName(evaluatorName).isPresent();
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
     * @return the existing or newly created evaluator
     */
    Evaluator createIfAbsent(CreateEvaluatorRequest request);

    /**
     * Convenience overload of {@link #createIfAbsent(CreateEvaluatorRequest)} for a code evaluator.
     *
     * @param request the code evaluator to create if it is missing
     * @return the existing or newly created evaluator
     */
    default Evaluator createIfAbsent(CreateCodeEvaluatorRequest1 request) {
        Objects.requireNonNull(request, "request must not be null");
        return createIfAbsent(new CreateEvaluatorRequest(request));
    }

    /**
     * Convenience overload of {@link #createIfAbsent(CreateEvaluatorRequest)} for an LLM-as-a-judge
     * evaluator.
     *
     * @param request the LLM-as-a-judge evaluator to create if it is missing
     * @return the existing or newly created evaluator
     */
    default Evaluator createIfAbsent(CreateLlmAsJudgeEvaluatorRequest1 request) {
        Objects.requireNonNull(request, "request must not be null");
        return createIfAbsent(new CreateEvaluatorRequest(request));
    }
}
