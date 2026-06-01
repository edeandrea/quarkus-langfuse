package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.ingestion.IngestionApi.APIIngestionBatchRequest;
import com.langfuse.api.model.IngestionBatchRequest;
import com.langfuse.api.model.IngestionResponse;

/**
 * Langfuse Ingestion Async API
 */
public interface QuarkusIngestionAsyncApi extends com.langfuse.api.ingestion.async.IngestionApi {

    /**
     * Batch ingestion for Langfuse Observability
     */
    @POST
    @Path("/api/public/ingestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<IngestionResponse> ingestionBatch(
            IngestionBatchRequest ingestionBatchRequest);

    /**
     * Batch ingestion for Langfuse Observability
     */
    @Override
    default CompletionStage<IngestionResponse> ingestionBatch(APIIngestionBatchRequest apiRequest) {
        return ingestionBatch(apiRequest.ingestionBatchRequest());
    }

}
