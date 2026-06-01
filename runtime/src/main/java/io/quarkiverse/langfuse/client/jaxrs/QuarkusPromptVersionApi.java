package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.Prompt;
import com.langfuse.api.model.PromptVersionUpdateRequest;
import com.langfuse.api.promptVersion.PromptVersionApi.APIPromptVersionUpdateRequest;

/**
 * Langfuse PromptVersion API
 */
public interface QuarkusPromptVersionApi extends com.langfuse.api.promptVersion.PromptVersionApi {

    /**
     * Update labels for a specific prompt version
     */
    @PATCH
    @Path("/api/public/v2/prompts/{name}/versions/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Prompt promptVersionUpdate(
            @PathParam("name") String name,
            @PathParam("version") Integer version,
            PromptVersionUpdateRequest promptVersionUpdateRequest);

    /**
     * Update labels for a specific prompt version
     */
    @Override
    default Prompt promptVersionUpdate(APIPromptVersionUpdateRequest apiRequest) {
        return promptVersionUpdate(apiRequest.name(), apiRequest.version(), apiRequest.promptVersionUpdateRequest());
    }

}
