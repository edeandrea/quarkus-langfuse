package io.quarkiverse.langfuse.client.jaxrs;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.CreatePromptRequest;
import com.langfuse.api.model.Prompt;
import com.langfuse.api.model.PromptMetaListResponse;
import com.langfuse.api.prompts.PromptsApi.APIPromptsCreateRequest;
import com.langfuse.api.prompts.PromptsApi.APIPromptsDeleteRequest;
import com.langfuse.api.prompts.PromptsApi.APIPromptsGetRequest;
import com.langfuse.api.prompts.PromptsApi.APIPromptsListRequest;

/**
 * Langfuse Prompts Async API
 */
public interface QuarkusPromptsAsyncApi extends com.langfuse.api.prompts.async.PromptsApi {

    /**
     * Create a new version for the prompt with the given name
     */
    @POST
    @Path("/api/public/v2/prompts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Prompt> promptsCreate(
            CreatePromptRequest createPromptRequest);

    /**
     * Create a new version for the prompt with the given name
     */
    @Override
    default CompletionStage<Prompt> promptsCreate(APIPromptsCreateRequest apiRequest) {
        return promptsCreate(apiRequest.createPromptRequest());
    }

    /**
     * Delete prompt versions.
     */
    @DELETE
    @Path("/api/public/v2/prompts/{promptName}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Void> promptsDelete(
            @PathParam("promptName") String promptName,
            @QueryParam("label") String label,
            @QueryParam("version") Integer version);

    /**
     * Delete prompt versions.
     */
    @Override
    default CompletionStage<Void> promptsDelete(APIPromptsDeleteRequest apiRequest) {
        return promptsDelete(apiRequest.promptName(), apiRequest.label(), apiRequest.version());
    }

    /**
     * Get a prompt
     */
    @GET
    @Path("/api/public/v2/prompts/{promptName}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Prompt> promptsGet(
            @PathParam("promptName") String promptName,
            @QueryParam("version") Integer version,
            @QueryParam("label") String label,
            @QueryParam("resolve") Boolean resolve);

    /**
     * Get a prompt
     */
    @Override
    default CompletionStage<Prompt> promptsGet(APIPromptsGetRequest apiRequest) {
        return promptsGet(apiRequest.promptName(), apiRequest.version(), apiRequest.label(), apiRequest.resolve());
    }

    /**
     * Get a list of prompt names with versions and labels
     */
    @GET
    @Path("/api/public/v2/prompts")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<PromptMetaListResponse> promptsList(
            @QueryParam("name") String name,
            @QueryParam("label") String label,
            @QueryParam("tag") String tag,
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit,
            @QueryParam("fromUpdatedAt") OffsetDateTime fromUpdatedAt,
            @QueryParam("toUpdatedAt") OffsetDateTime toUpdatedAt);

    /**
     * Get a list of prompt names with versions and labels
     */
    @Override
    default CompletionStage<PromptMetaListResponse> promptsList(APIPromptsListRequest apiRequest) {
        return promptsList(apiRequest.name(), apiRequest.label(), apiRequest.tag(), apiRequest.page(), apiRequest.limit(),
                apiRequest.fromUpdatedAt(), apiRequest.toUpdatedAt());
    }

}
