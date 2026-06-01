package io.quarkiverse.langfuse.client.jaxrs;

import java.time.OffsetDateTime;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.datasetItems.DatasetItemsApi.APIDatasetItemsCreateRequest;
import com.langfuse.api.datasetItems.DatasetItemsApi.APIDatasetItemsDeleteRequest;
import com.langfuse.api.datasetItems.DatasetItemsApi.APIDatasetItemsGetRequest;
import com.langfuse.api.datasetItems.DatasetItemsApi.APIDatasetItemsListRequest;
import com.langfuse.api.model.CreateDatasetItemRequest;
import com.langfuse.api.model.DatasetItem;
import com.langfuse.api.model.DeleteDatasetItemResponse;
import com.langfuse.api.model.PaginatedDatasetItems;

/**
 * Langfuse DatasetItems API
 */
public interface QuarkusDatasetItemsApi extends com.langfuse.api.datasetItems.DatasetItemsApi {

    /**
     * Create a dataset item
     */
    @POST
    @Path("/api/public/dataset-items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DatasetItem datasetItemsCreate(
            CreateDatasetItemRequest createDatasetItemRequest);

    /**
     * Create a dataset item
     */
    @Override
    default DatasetItem datasetItemsCreate(APIDatasetItemsCreateRequest apiRequest) {
        return datasetItemsCreate(apiRequest.createDatasetItemRequest());
    }

    /**
     * Delete a dataset item and all its run items.
     */
    @DELETE
    @Path("/api/public/dataset-items/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    DeleteDatasetItemResponse datasetItemsDelete(
            @PathParam("id") String id);

    /**
     * Delete a dataset item and all its run items.
     */
    @Override
    default DeleteDatasetItemResponse datasetItemsDelete(APIDatasetItemsDeleteRequest apiRequest) {
        return datasetItemsDelete(apiRequest.id());
    }

    /**
     * Get a dataset item
     */
    @GET
    @Path("/api/public/dataset-items/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    DatasetItem datasetItemsGet(
            @PathParam("id") String id);

    /**
     * Get a dataset item
     */
    @Override
    default DatasetItem datasetItemsGet(APIDatasetItemsGetRequest apiRequest) {
        return datasetItemsGet(apiRequest.id());
    }

    /**
     * Get dataset items.
     */
    @GET
    @Path("/api/public/dataset-items")
    @Produces(MediaType.APPLICATION_JSON)
    PaginatedDatasetItems datasetItemsList(
            @QueryParam("datasetName") String datasetName,
            @QueryParam("sourceTraceId") String sourceTraceId,
            @QueryParam("sourceObservationId") String sourceObservationId,
            @QueryParam("version") OffsetDateTime version,
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit);

    /**
     * Get dataset items.
     */
    @Override
    default PaginatedDatasetItems datasetItemsList(APIDatasetItemsListRequest apiRequest) {
        return datasetItemsList(apiRequest.datasetName(), apiRequest.sourceTraceId(), apiRequest.sourceObservationId(),
                apiRequest.version(), apiRequest.page(), apiRequest.limit());
    }

}
