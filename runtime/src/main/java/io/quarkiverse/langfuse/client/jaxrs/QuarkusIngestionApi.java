package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.ingestion.IngestionApi.APIIngestionBatchRequest;
import com.langfuse.api.model.IngestionBatchRequest;
import com.langfuse.api.model.IngestionResponse;

/**
 * Langfuse Ingestion API
 */
public interface QuarkusIngestionApi extends com.langfuse.api.ingestion.IngestionApi {

    /**
     * Batch ingestion for Langfuse Observability
     */
    @POST
    @Path("/api/public/ingestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    IngestionResponse ingestionBatch(
            IngestionBatchRequest ingestionBatchRequest);

    /**
     * Batch ingestion for Langfuse Observability
     */
    @Override
    default IngestionResponse ingestionBatch(APIIngestionBatchRequest apiRequest) {
        return ingestionBatch(apiRequest.ingestionBatchRequest());
    }

}
