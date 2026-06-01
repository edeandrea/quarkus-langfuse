package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.CreateScoreConfigRequest;
import com.langfuse.api.model.ScoreConfig;
import com.langfuse.api.model.ScoreConfigs;
import com.langfuse.api.model.UpdateScoreConfigRequest;
import com.langfuse.api.scoreConfigs.ScoreConfigsApi.APIScoreConfigsCreateRequest;
import com.langfuse.api.scoreConfigs.ScoreConfigsApi.APIScoreConfigsGetByIdRequest;
import com.langfuse.api.scoreConfigs.ScoreConfigsApi.APIScoreConfigsGetRequest;
import com.langfuse.api.scoreConfigs.ScoreConfigsApi.APIScoreConfigsUpdateRequest;

/**
 * Langfuse ScoreConfigs Async API
 */
public interface QuarkusScoreConfigsAsyncApi extends com.langfuse.api.scoreConfigs.async.ScoreConfigsApi {

    /**
     * Create a score configuration (config).
     */
    @POST
    @Path("/api/public/score-configs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ScoreConfig> scoreConfigsCreate(
            CreateScoreConfigRequest createScoreConfigRequest);

    /**
     * Create a score configuration (config).
     */
    @Override
    default CompletionStage<ScoreConfig> scoreConfigsCreate(APIScoreConfigsCreateRequest apiRequest) {
        return scoreConfigsCreate(apiRequest.createScoreConfigRequest());
    }

    /**
     * Get all score configs
     */
    @GET
    @Path("/api/public/score-configs")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ScoreConfigs> scoreConfigsGet(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit);

    /**
     * Get all score configs
     */
    @Override
    default CompletionStage<ScoreConfigs> scoreConfigsGet(APIScoreConfigsGetRequest apiRequest) {
        return scoreConfigsGet(apiRequest.page(), apiRequest.limit());
    }

    /**
     * Get a score config
     */
    @GET
    @Path("/api/public/score-configs/{configId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ScoreConfig> scoreConfigsGetById(
            @PathParam("configId") String configId);

    /**
     * Get a score config
     */
    @Override
    default CompletionStage<ScoreConfig> scoreConfigsGetById(APIScoreConfigsGetByIdRequest apiRequest) {
        return scoreConfigsGetById(apiRequest.configId());
    }

    /**
     * Update a score config
     */
    @PATCH
    @Path("/api/public/score-configs/{configId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ScoreConfig> scoreConfigsUpdate(
            @PathParam("configId") String configId,
            UpdateScoreConfigRequest updateScoreConfigRequest);

    /**
     * Update a score config
     */
    @Override
    default CompletionStage<ScoreConfig> scoreConfigsUpdate(APIScoreConfigsUpdateRequest apiRequest) {
        return scoreConfigsUpdate(apiRequest.configId(), apiRequest.updateScoreConfigRequest());
    }

}
