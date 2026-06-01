package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.blobStorageIntegrations.BlobStorageIntegrationsApi.APIBlobStorageIntegrationsDeleteBlobStorageIntegrationRequest;
import com.langfuse.api.blobStorageIntegrations.BlobStorageIntegrationsApi.APIBlobStorageIntegrationsGetBlobStorageIntegrationStatusRequest;
import com.langfuse.api.blobStorageIntegrations.BlobStorageIntegrationsApi.APIBlobStorageIntegrationsUpsertBlobStorageIntegrationRequest;
import com.langfuse.api.model.BlobStorageIntegrationDeletionResponse;
import com.langfuse.api.model.BlobStorageIntegrationResponse;
import com.langfuse.api.model.BlobStorageIntegrationStatusResponse;
import com.langfuse.api.model.BlobStorageIntegrationsResponse;
import com.langfuse.api.model.CreateBlobStorageIntegrationRequest;

/**
 * Langfuse Blob Storage Integrations API
 */
public interface QuarkusBlobStorageIntegrationsApi
        extends com.langfuse.api.blobStorageIntegrations.BlobStorageIntegrationsApi {

    /**
     * Delete a blob storage integration by ID
     */
    @DELETE
    @Path("/api/public/integrations/blob-storage/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    BlobStorageIntegrationDeletionResponse blobStorageIntegrationsDeleteBlobStorageIntegration(
            @PathParam("id") String id);

    /**
     * Delete a blob storage integration by ID
     */
    @Override
    default BlobStorageIntegrationDeletionResponse blobStorageIntegrationsDeleteBlobStorageIntegration(
            APIBlobStorageIntegrationsDeleteBlobStorageIntegrationRequest apiRequest) {
        return blobStorageIntegrationsDeleteBlobStorageIntegration(apiRequest.id());
    }

    /**
     * Get the sync status of a blob storage integration by integration ID
     */
    @GET
    @Path("/api/public/integrations/blob-storage/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    BlobStorageIntegrationStatusResponse blobStorageIntegrationsGetBlobStorageIntegrationStatus(
            @PathParam("id") String id);

    /**
     * Get the sync status of a blob storage integration by integration ID
     */
    @Override
    default BlobStorageIntegrationStatusResponse blobStorageIntegrationsGetBlobStorageIntegrationStatus(
            APIBlobStorageIntegrationsGetBlobStorageIntegrationStatusRequest apiRequest) {
        return blobStorageIntegrationsGetBlobStorageIntegrationStatus(apiRequest.id());
    }

    /**
     * Get all blob storage integrations for the organization
     */
    @GET
    @Path("/api/public/integrations/blob-storage")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    BlobStorageIntegrationsResponse blobStorageIntegrationsGetBlobStorageIntegrations();

    /**
     * Create or update a blob storage integration for a specific project
     */
    @PUT
    @Path("/api/public/integrations/blob-storage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BlobStorageIntegrationResponse blobStorageIntegrationsUpsertBlobStorageIntegration(
            CreateBlobStorageIntegrationRequest createBlobStorageIntegrationRequest);

    /**
     * Create or update a blob storage integration for a specific project
     */
    @Override
    default BlobStorageIntegrationResponse blobStorageIntegrationsUpsertBlobStorageIntegration(
            APIBlobStorageIntegrationsUpsertBlobStorageIntegrationRequest apiRequest) {
        return blobStorageIntegrationsUpsertBlobStorageIntegration(apiRequest.createBlobStorageIntegrationRequest());
    }

}
