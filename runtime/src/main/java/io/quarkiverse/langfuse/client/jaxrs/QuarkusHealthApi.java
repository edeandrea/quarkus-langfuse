package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.HealthResponse;

/**
 * Langfuse Health API
 */
public interface QuarkusHealthApi extends com.langfuse.api.health.HealthApi {

    /**
     * Check health of API and database
     */
    @GET
    @Path("/api/public/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    HealthResponse healthHealth();

}
