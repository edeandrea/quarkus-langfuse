package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.HealthResponse;

/**
 * Langfuse Health Async API
 */
public interface QuarkusHealthAsyncApi extends com.langfuse.api.health.async.HealthApi {

    /**
     * Check health of API and database
     */
    @GET
    @Path("/api/public/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    CompletionStage<HealthResponse> healthHealth();

}
