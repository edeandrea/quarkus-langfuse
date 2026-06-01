package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.datasets.DatasetsApi.APIDatasetsCreateRequest;
import com.langfuse.api.datasets.DatasetsApi.APIDatasetsDeleteRunRequest;
import com.langfuse.api.datasets.DatasetsApi.APIDatasetsGetRequest;
import com.langfuse.api.datasets.DatasetsApi.APIDatasetsGetRunRequest;
import com.langfuse.api.datasets.DatasetsApi.APIDatasetsGetRunsRequest;
import com.langfuse.api.datasets.DatasetsApi.APIDatasetsListRequest;
import com.langfuse.api.model.CreateDatasetRequest;
import com.langfuse.api.model.Dataset;
import com.langfuse.api.model.DatasetRunWithItems;
import com.langfuse.api.model.DeleteDatasetRunResponse;
import com.langfuse.api.model.PaginatedDatasetRuns;
import com.langfuse.api.model.PaginatedDatasets;

/**
 * Langfuse Datasets API
 */
public interface QuarkusDatasetsApi extends com.langfuse.api.datasets.DatasetsApi {

    /**
     * Create a dataset
     */
    @POST
    @Path("/api/public/v2/datasets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Dataset datasetsCreate(
            CreateDatasetRequest createDatasetRequest);

    /**
     * Create a dataset
     */
    @Override
    default Dataset datasetsCreate(APIDatasetsCreateRequest apiRequest) {
        return datasetsCreate(apiRequest.createDatasetRequest());
    }

    /**
     * Delete a dataset run and all its run items.
     */
    @DELETE
    @Path("/api/public/datasets/{datasetName}/runs/{runName}")
    @Produces(MediaType.APPLICATION_JSON)
    DeleteDatasetRunResponse datasetsDeleteRun(
            @PathParam("datasetName") String datasetName,
            @PathParam("runName") String runName);

    /**
     * Delete a dataset run and all its run items.
     */
    @Override
    default DeleteDatasetRunResponse datasetsDeleteRun(APIDatasetsDeleteRunRequest apiRequest) {
        return datasetsDeleteRun(apiRequest.datasetName(), apiRequest.runName());
    }

    /**
     * Get a dataset
     */
    @GET
    @Path("/api/public/v2/datasets/{datasetName}")
    @Produces(MediaType.APPLICATION_JSON)
    Dataset datasetsGet(
            @PathParam("datasetName") String datasetName);

    /**
     * Get a dataset
     */
    @Override
    default Dataset datasetsGet(APIDatasetsGetRequest apiRequest) {
        return datasetsGet(apiRequest.datasetName());
    }

    /**
     * Get a dataset run and its items
     */
    @GET
    @Path("/api/public/datasets/{datasetName}/runs/{runName}")
    @Produces(MediaType.APPLICATION_JSON)
    DatasetRunWithItems datasetsGetRun(
            @PathParam("datasetName") String datasetName,
            @PathParam("runName") String runName);

    /**
     * Get a dataset run and its items
     */
    @Override
    default DatasetRunWithItems datasetsGetRun(APIDatasetsGetRunRequest apiRequest) {
        return datasetsGetRun(apiRequest.datasetName(), apiRequest.runName());
    }

    /**
     * Get dataset runs
     */
    @GET
    @Path("/api/public/datasets/{datasetName}/runs")
    @Produces(MediaType.APPLICATION_JSON)
    PaginatedDatasetRuns datasetsGetRuns(
            @PathParam("datasetName") String datasetName,
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit);

    /**
     * Get dataset runs
     */
    @Override
    default PaginatedDatasetRuns datasetsGetRuns(APIDatasetsGetRunsRequest apiRequest) {
        return datasetsGetRuns(apiRequest.datasetName(), apiRequest.page(), apiRequest.limit());
    }

    /**
     * Get all datasets
     */
    @GET
    @Path("/api/public/v2/datasets")
    @Produces(MediaType.APPLICATION_JSON)
    PaginatedDatasets datasetsList(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit);

    /**
     * Get all datasets
     */
    @Override
    default PaginatedDatasets datasetsList(APIDatasetsListRequest apiRequest) {
        return datasetsList(apiRequest.page(), apiRequest.limit());
    }

}
