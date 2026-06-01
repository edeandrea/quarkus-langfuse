package io.quarkiverse.langfuse.client.jaxrs;

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
 * Langfuse Media API
 */
public interface QuarkusMediaApi extends com.langfuse.api.media.MediaApi {

    /**
     * Get a media record
     */
    @GET
    @Path("/api/public/media/{mediaId}")
    @Produces(MediaType.APPLICATION_JSON)
    GetMediaResponse mediaGet(
            @PathParam("mediaId") String mediaId);

    /**
     * Get a media record
     */
    @Override
    default GetMediaResponse mediaGet(APIMediaGetRequest apiRequest) {
        return mediaGet(apiRequest.mediaId());
    }

    /**
     * Get a presigned upload URL for a media record
     */
    @POST
    @Path("/api/public/media")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    GetMediaUploadUrlResponse mediaGetUploadUrl(
            GetMediaUploadUrlRequest getMediaUploadUrlRequest);

    /**
     * Get a presigned upload URL for a media record
     */
    @Override
    default GetMediaUploadUrlResponse mediaGetUploadUrl(APIMediaGetUploadUrlRequest apiRequest) {
        return mediaGetUploadUrl(apiRequest.getMediaUploadUrlRequest());
    }

    /**
     * Patch a media record
     */
    @PATCH
    @Path("/api/public/media/{mediaId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void mediaPatch(
            @PathParam("mediaId") String mediaId,
            PatchMediaBody patchMediaBody);

    /**
     * Patch a media record
     */
    @Override
    default void mediaPatch(APIMediaPatchRequest apiRequest) {
        mediaPatch(apiRequest.mediaId(), apiRequest.patchMediaBody());
    }

}
