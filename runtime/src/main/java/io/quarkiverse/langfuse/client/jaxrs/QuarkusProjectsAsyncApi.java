package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.ApiKeyDeletionResponse;
import com.langfuse.api.model.ApiKeyList;
import com.langfuse.api.model.ApiKeyResponse;
import com.langfuse.api.model.Project;
import com.langfuse.api.model.ProjectDeletionResponse;
import com.langfuse.api.model.Projects;
import com.langfuse.api.model.ProjectsCreateApiKeyRequest;
import com.langfuse.api.model.ProjectsCreateRequest;
import com.langfuse.api.model.ProjectsUpdateRequest;
import com.langfuse.api.projects.ProjectsApi.APIProjectsCreateApiKeyRequest;
import com.langfuse.api.projects.ProjectsApi.APIProjectsCreateRequest;
import com.langfuse.api.projects.ProjectsApi.APIProjectsDeleteApiKeyRequest;
import com.langfuse.api.projects.ProjectsApi.APIProjectsDeleteRequest;
import com.langfuse.api.projects.ProjectsApi.APIProjectsGetApiKeysRequest;
import com.langfuse.api.projects.ProjectsApi.APIProjectsUpdateRequest;

public interface QuarkusProjectsAsyncApi extends com.langfuse.api.projects.async.ProjectsApi {

    /**
     * Create a new project
     */
    @POST
    @Path("/api/public/projects")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Project> projectsCreate(
            ProjectsCreateRequest projectsCreateRequest);

    /**
     * Create a new project
     */
    @Override
    default CompletionStage<Project> projectsCreate(APIProjectsCreateRequest apiRequest) {
        return projectsCreate(apiRequest.projectsCreateRequest());
    }

    /**
     * Create a new API key for a project
     */
    @POST
    @Path("/api/public/projects/{projectId}/apiKeys")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ApiKeyResponse> projectsCreateApiKey(
            @PathParam("projectId") String projectId,
            ProjectsCreateApiKeyRequest projectsCreateApiKeyRequest);

    /**
     * Create a new API key for a project
     */
    @Override
    default CompletionStage<ApiKeyResponse> projectsCreateApiKey(APIProjectsCreateApiKeyRequest apiRequest) {
        return projectsCreateApiKey(apiRequest.projectId(), apiRequest.projectsCreateApiKeyRequest());
    }

    /**
     * Delete a project by ID
     */
    @DELETE
    @Path("/api/public/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ProjectDeletionResponse> projectsDelete(
            @PathParam("projectId") String projectId);

    /**
     * Delete a project by ID
     */
    @Override
    default CompletionStage<ProjectDeletionResponse> projectsDelete(APIProjectsDeleteRequest apiRequest) {
        return projectsDelete(apiRequest.projectId());
    }

    /**
     * Delete an API key for a project
     */
    @DELETE
    @Path("/api/public/projects/{projectId}/apiKeys/{apiKeyId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ApiKeyDeletionResponse> projectsDeleteApiKey(
            @PathParam("projectId") String projectId,
            @PathParam("apiKeyId") String apiKeyId);

    /**
     * Delete an API key for a project
     */
    @Override
    default CompletionStage<ApiKeyDeletionResponse> projectsDeleteApiKey(APIProjectsDeleteApiKeyRequest apiRequest) {
        return projectsDeleteApiKey(apiRequest.projectId(), apiRequest.apiKeyId());
    }

    /**
     * Get Project associated with API key
     */
    @GET
    @Path("/api/public/projects")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    CompletionStage<Projects> projectsGet();

    /**
     * Get all API keys for a project
     */
    @GET
    @Path("/api/public/projects/{projectId}/apiKeys")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<ApiKeyList> projectsGetApiKeys(
            @PathParam("projectId") String projectId);

    /**
     * Get all API keys for a project
     */
    @Override
    default CompletionStage<ApiKeyList> projectsGetApiKeys(APIProjectsGetApiKeysRequest apiRequest) {
        return projectsGetApiKeys(apiRequest.projectId());
    }

    /**
     * Update a project by ID
     */
    @PUT
    @Path("/api/public/projects/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Project> projectsUpdate(
            @PathParam("projectId") String projectId,
            ProjectsUpdateRequest projectsUpdateRequest);

    /**
     * Update a project by ID
     */
    @Override
    default CompletionStage<Project> projectsUpdate(APIProjectsUpdateRequest apiRequest) {
        return projectsUpdate(apiRequest.projectId(), apiRequest.projectsUpdateRequest());
    }

}
