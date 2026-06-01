package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.datasetRunItems.DatasetRunItemsApi.APIDatasetRunItemsCreateRequest;
import com.langfuse.api.datasetRunItems.DatasetRunItemsApi.APIDatasetRunItemsListRequest;
import com.langfuse.api.model.CreateDatasetRunItemRequest;
import com.langfuse.api.model.DatasetRunItem;
import com.langfuse.api.model.PaginatedDatasetRunItems;

/**
 * Langfuse DatasetRunItems API
 */
public interface QuarkusDatasetRunItemsApi extends com.langfuse.api.datasetRunItems.DatasetRunItemsApi {

    /**
     * Create a dataset run item
     */
    @POST
    @Path("/api/public/dataset-run-items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DatasetRunItem datasetRunItemsCreate(
            CreateDatasetRunItemRequest createDatasetRunItemRequest);

    /**
     * Create a dataset run item
     */
    @Override
    default DatasetRunItem datasetRunItemsCreate(APIDatasetRunItemsCreateRequest apiRequest) {
        return datasetRunItemsCreate(apiRequest.createDatasetRunItemRequest());
    }

    /**
     * List dataset run items
     */
    @GET
    @Path("/api/public/dataset-run-items")
    @Produces(MediaType.APPLICATION_JSON)
    PaginatedDatasetRunItems datasetRunItemsList(
            @QueryParam("datasetId") String datasetId,
            @QueryParam("runName") String runName,
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit);

    /**
     * List dataset run items
     */
    @Override
    default PaginatedDatasetRunItems datasetRunItemsList(APIDatasetRunItemsListRequest apiRequest) {
        return datasetRunItemsList(apiRequest.datasetId(), apiRequest.runName(), apiRequest.page(), apiRequest.limit());
    }

}
