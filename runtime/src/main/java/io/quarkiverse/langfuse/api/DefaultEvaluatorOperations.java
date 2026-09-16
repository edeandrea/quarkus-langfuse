package io.quarkiverse.langfuse.api;

import java.util.Optional;

import com.langfuse.api.evaluators.EvaluatorsApi;
import com.langfuse.api.evaluators.EvaluatorsApi.APIEvaluatorsCreateRequest;
import com.langfuse.api.evaluators.EvaluatorsApi.APIEvaluatorsListRequest;
import com.langfuse.api.model.CreateEvaluatorRequest;
import com.langfuse.api.model.Evaluator;

import io.quarkiverse.langfuse.config.LangfuseConfig;

final class DefaultEvaluatorOperations extends AbstractCursorOperations<Evaluator>
        implements EvaluatorOperations {
    private final EvaluatorsApi evaluatorsApi;

    DefaultEvaluatorOperations(EvaluatorsApi evaluatorsApi, LangfuseConfig config) {
        super(cursor -> fetch(evaluatorsApi, cursor), config);
        this.evaluatorsApi = evaluatorsApi;
    }

    @Override
    public Optional<Evaluator> findByName(String evaluatorName) {
        return scanForName(Names.require(evaluatorName, "Evaluator name"), EvaluatorOperationsBase::nameOf);
    }

    @Override
    public Evaluator createIfAbsent(CreateEvaluatorRequest request) {
        var name = Names.require(EvaluatorOperationsBase.nameOf(request), "Evaluator name");
        return findByName(name)
                .orElseGet(() -> this.evaluatorsApi.evaluatorsCreate(APIEvaluatorsCreateRequest.newBuilder()
                        .createEvaluatorRequest(request)
                        .build()));
    }

    private static CursorResult<Evaluator> fetch(EvaluatorsApi evaluatorsApi, Cursor cursor) {
        var response = evaluatorsApi.evaluatorsList(APIEvaluatorsListRequest.newBuilder()
                .limit(cursor.limit())
                .cursor(cursor.value().orElse(null))
                .build());

        return CursorResults.from(cursor, response.getData(), response.getMeta());
    }
}
