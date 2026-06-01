package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.OpentelemetryExportTracesRequest;
import com.langfuse.api.opentelemetry.OpentelemetryApi.APIOpentelemetryExportTracesRequest;

/**
 * Langfuse Opentelemetry API
 */
public interface QuarkusOpentelemetryApi extends com.langfuse.api.opentelemetry.OpentelemetryApi {

    /**
     * OpenTelemetry Traces Ingestion Endpoint
     */
    @POST
    @Path("/api/public/otel/v1/traces")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Object opentelemetryExportTraces(
            OpentelemetryExportTracesRequest opentelemetryExportTracesRequest);

    /**
     * OpenTelemetry Traces Ingestion Endpoint
     */
    @Override
    default Object opentelemetryExportTraces(APIOpentelemetryExportTracesRequest apiRequest) {
        return opentelemetryExportTraces(apiRequest.opentelemetryExportTracesRequest());
    }

}
