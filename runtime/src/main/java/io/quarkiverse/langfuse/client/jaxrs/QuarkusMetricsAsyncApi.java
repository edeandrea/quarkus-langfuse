package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.metrics.MetricsApi.APIMetricsMetricsRequest;
import com.langfuse.api.model.MetricsV2Response;

/**
 * Langfuse Metrics Async API
 */
public interface QuarkusMetricsAsyncApi extends com.langfuse.api.metrics.async.MetricsApi {

    /**
     * Get metrics from the Langfuse project
     */
    @GET
    @Path("/api/public/v2/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<MetricsV2Response> metricsMetrics(
            @QueryParam("query") String query);

    /**
     * Get metrics from the Langfuse project
     */
    @Override
    default CompletionStage<MetricsV2Response> metricsMetrics(APIMetricsMetricsRequest apiRequest) {
        return metricsMetrics(apiRequest.query());
    }

}
