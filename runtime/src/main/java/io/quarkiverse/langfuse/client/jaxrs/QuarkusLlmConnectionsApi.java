package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.llmConnections.LlmConnectionsApi.APILlmConnectionsDeleteRequest;
import com.langfuse.api.llmConnections.LlmConnectionsApi.APILlmConnectionsListRequest;
import com.langfuse.api.llmConnections.LlmConnectionsApi.APILlmConnectionsUpsertRequest;
import com.langfuse.api.model.DeleteLlmConnectionResponse;
import com.langfuse.api.model.LlmConnection;
import com.langfuse.api.model.PaginatedLlmConnections;
import com.langfuse.api.model.UpsertLlmConnectionRequest;

/**
 * Langfuse LLM Connections API
 */
public interface QuarkusLlmConnectionsApi extends com.langfuse.api.llmConnections.LlmConnectionsApi {

    /**
     * Delete an LLM connection by id
     */
    @DELETE
    @Path("/api/public/llm-connections/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    DeleteLlmConnectionResponse llmConnectionsDelete(
            @PathParam("id") String id);

    /**
     * Delete an LLM connection by id
     */
    @Override
    default DeleteLlmConnectionResponse llmConnectionsDelete(APILlmConnectionsDeleteRequest apiRequest) {
        return llmConnectionsDelete(apiRequest.id());
    }

    /**
     * Get all LLM connections in a project
     */
    @GET
    @Path("/api/public/llm-connections")
    @Produces(MediaType.APPLICATION_JSON)
    PaginatedLlmConnections llmConnectionsList(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit);

    /**
     * Get all LLM connections in a project
     */
    @Override
    default PaginatedLlmConnections llmConnectionsList(APILlmConnectionsListRequest apiRequest) {
        return llmConnectionsList(apiRequest.page(), apiRequest.limit());
    }

    /**
     * Create or update an LLM connection
     */
    @PUT
    @Path("/api/public/llm-connections")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    LlmConnection llmConnectionsUpsert(
            UpsertLlmConnectionRequest upsertLlmConnectionRequest);

    /**
     * Create or update an LLM connection
     */
    @Override
    default LlmConnection llmConnectionsUpsert(APILlmConnectionsUpsertRequest apiRequest) {
        return llmConnectionsUpsert(apiRequest.upsertLlmConnectionRequest());
    }

}
