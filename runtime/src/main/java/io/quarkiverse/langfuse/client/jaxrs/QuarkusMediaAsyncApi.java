package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.media.MediaApi.APIMediaGetRequest;
import com.langfuse.api.media.MediaApi.APIMediaGetUploadUrlRequest;
import com.langfuse.api.media.MediaApi.APIMediaPatchRequest;
import com.langfuse.api.model.GetMediaResponse;
import com.langfuse.api.model.GetMediaUploadUrlRequest;
import com.langfuse.api.model.GetMediaUploadUrlResponse;
import com.langfuse.api.model.PatchMediaBody;

/**
 * Langfuse Media Async API
 */
public interface QuarkusMediaAsyncApi extends com.langfuse.api.media.async.MediaApi {

    /**
     * Get a media record
     */
    @GET
    @Path("/api/public/media/{mediaId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<GetMediaResponse> mediaGet(
            @PathParam("mediaId") String mediaId);

    /**
     * Get a media record
     */
    @Override
    default CompletionStage<GetMediaResponse> mediaGet(APIMediaGetRequest apiRequest) {
        return mediaGet(apiRequest.mediaId());
    }

    /**
     * Get a presigned upload URL for a media record
     */
    @POST
    @Path("/api/public/media")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<GetMediaUploadUrlResponse> mediaGetUploadUrl(
            GetMediaUploadUrlRequest getMediaUploadUrlRequest);

    /**
     * Get a presigned upload URL for a media record
     */
    @Override
    default CompletionStage<GetMediaUploadUrlResponse> mediaGetUploadUrl(APIMediaGetUploadUrlRequest apiRequest) {
        return mediaGetUploadUrl(apiRequest.getMediaUploadUrlRequest());
    }

    /**
     * Patch a media record
     */
    @PATCH
    @Path("/api/public/media/{mediaId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Void> mediaPatch(
            @PathParam("mediaId") String mediaId,
            PatchMediaBody patchMediaBody);

    /**
     * Patch a media record
     */
    @Override
    default CompletionStage<Void> mediaPatch(APIMediaPatchRequest apiRequest) {
        return mediaPatch(apiRequest.mediaId(), apiRequest.patchMediaBody());
    }

}
