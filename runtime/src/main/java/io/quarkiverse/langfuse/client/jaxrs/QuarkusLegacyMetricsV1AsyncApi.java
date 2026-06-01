package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.legacyMetricsV1.LegacyMetricsV1Api.APILegacyMetricsV1MetricsRequest;
import com.langfuse.api.model.LegacyMetricsResponse;

/**
 * Langfuse Legacy Metrics V1 Async API
 */
public interface QuarkusLegacyMetricsV1AsyncApi extends com.langfuse.api.legacyMetricsV1.async.LegacyMetricsV1Api {

    /**
     * Get metrics from the Langfuse project
     */
    @GET
    @Path("/api/public/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<LegacyMetricsResponse> legacyMetricsV1Metrics(
            @QueryParam("query") String query);

    /**
     * Get metrics from the Langfuse project
     */
    @Override
    default CompletionStage<LegacyMetricsResponse> legacyMetricsV1Metrics(
            APILegacyMetricsV1MetricsRequest apiRequest) {
        return legacyMetricsV1Metrics(apiRequest.query());
    }

}
