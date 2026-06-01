package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.legacyScoreV1.LegacyScoreV1Api.APILegacyScoreV1CreateRequest;
import com.langfuse.api.legacyScoreV1.LegacyScoreV1Api.APILegacyScoreV1DeleteRequest;
import com.langfuse.api.model.LegacyCreateScoreRequest;
import com.langfuse.api.model.LegacyCreateScoreResponse;

/**
 * Langfuse LegacyScoreV1 Async API
 */
public interface QuarkusLegacyScoreV1AsyncApi extends com.langfuse.api.legacyScoreV1.async.LegacyScoreV1Api {

    /**
     * Create a score (supports both trace and session scores)
     */
    @POST
    @Path("/api/public/scores")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<LegacyCreateScoreResponse> legacyScoreV1Create(
            LegacyCreateScoreRequest legacyCreateScoreRequest);

    /**
     * Create a score (supports both trace and session scores)
     */
    @Override
    default CompletionStage<LegacyCreateScoreResponse> legacyScoreV1Create(APILegacyScoreV1CreateRequest apiRequest) {
        return legacyScoreV1Create(apiRequest.legacyCreateScoreRequest());
    }

    /**
     * Delete a score (supports both trace and session scores)
     */
    @DELETE
    @Path("/api/public/scores/{scoreId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Void> legacyScoreV1Delete(
            @PathParam("scoreId") String scoreId);

    /**
     * Delete a score (supports both trace and session scores)
     */
    @Override
    default CompletionStage<Void> legacyScoreV1Delete(APILegacyScoreV1DeleteRequest apiRequest) {
        return legacyScoreV1Delete(apiRequest.scoreId());
    }

}
