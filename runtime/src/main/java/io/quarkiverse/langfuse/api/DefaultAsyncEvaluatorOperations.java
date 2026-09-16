package io.quarkiverse.langfuse.api;

import com.langfuse.api.evaluators.EvaluatorsApi.APIEvaluatorsCreateRequest;
import com.langfuse.api.evaluators.EvaluatorsApi.APIEvaluatorsListRequest;
import com.langfuse.api.evaluators.async.EvaluatorsApi;
import com.langfuse.api.model.CreateEvaluatorRequest;
import com.langfuse.api.model.Evaluator;

import io.quarkiverse.langfuse.config.LangfuseConfig;
import io.smallrye.mutiny.Uni;

final class DefaultAsyncEvaluatorOperations extends AbstractAsyncCursorOperations<Evaluator>
        implements AsyncEvaluatorOperations {
    private final EvaluatorsApi evaluatorsApi;

    DefaultAsyncEvaluatorOperations(EvaluatorsApi evaluatorsApi, LangfuseConfig config) {
        super(cursor -> fetch(evaluatorsApi, cursor), config);
        this.evaluatorsApi = evaluatorsApi;
    }

    @Override
    public Uni<Evaluator> findByName(String evaluatorName) {
        return scanForName(Names.require(evaluatorName, "Evaluator name"), EvaluatorOperationsBase::nameOf);
    }

    @Override
    public Uni<Evaluator> createIfAbsent(CreateEvaluatorRequest request) {
        var name = Names.require(EvaluatorOperationsBase.nameOf(request), "Evaluator name");
        return findByName(name)
                .flatMap(existing -> (existing != null)
                        ? Uni.createFrom().item(existing)
                        : Uni.createFrom().completionStage(() -> this.evaluatorsApi.evaluatorsCreate(
                                APIEvaluatorsCreateRequest.newBuilder()
                                        .createEvaluatorRequest(request)
                                        .build())));
    }

    private static Uni<CursorResult<Evaluator>> fetch(EvaluatorsApi evaluatorsApi, Cursor cursor) {
        return Uni.createFrom()
                .completionStage(() -> evaluatorsApi.evaluatorsList(APIEvaluatorsListRequest.newBuilder()
                        .limit(cursor.limit())
                        .cursor(cursor.value().orElse(null))
                        .build()))
                .map(response -> CursorResults.from(cursor, response.getData(), response.getMeta()));
    }
}
