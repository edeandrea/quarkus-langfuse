package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.OpentelemetryExportTracesRequest;
import com.langfuse.api.opentelemetry.OpentelemetryApi.APIOpentelemetryExportTracesRequest;

/**
 * Langfuse Opentelemetry Async API
 */
public interface QuarkusOpentelemetryAsyncApi extends com.langfuse.api.opentelemetry.async.OpentelemetryApi {

    /**
     * OpenTelemetry Traces Ingestion Endpoint
     */
    @POST
    @Path("/api/public/otel/v1/traces")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Object> opentelemetryExportTraces(
            OpentelemetryExportTracesRequest opentelemetryExportTracesRequest);

    /**
     * OpenTelemetry Traces Ingestion Endpoint
     */
    @Override
    default CompletionStage<Object> opentelemetryExportTraces(APIOpentelemetryExportTracesRequest apiRequest) {
        return opentelemetryExportTraces(apiRequest.opentelemetryExportTracesRequest());
    }

}
