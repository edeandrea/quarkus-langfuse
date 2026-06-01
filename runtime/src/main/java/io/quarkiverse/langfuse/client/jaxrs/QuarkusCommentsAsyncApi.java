package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.comments.CommentsApi.APICommentsCreateRequest;
import com.langfuse.api.comments.CommentsApi.APICommentsGetByIdRequest;
import com.langfuse.api.comments.CommentsApi.APICommentsGetRequest;
import com.langfuse.api.model.Comment;
import com.langfuse.api.model.CreateCommentRequest;
import com.langfuse.api.model.CreateCommentResponse;
import com.langfuse.api.model.GetCommentsResponse;

/**
 * Langfuse Comments Async API
 */
public interface QuarkusCommentsAsyncApi extends com.langfuse.api.comments.async.CommentsApi {

    /**
     * Create a comment. Comments may be attached to different object types (trace, observation, session, prompt).
     */
    @POST
    @Path("/api/public/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<CreateCommentResponse> commentsCreate(
            CreateCommentRequest createCommentRequest);

    /**
     * Create a comment. Comments may be attached to different object types (trace, observation, session, prompt).
     */
    @Override
    default CompletionStage<CreateCommentResponse> commentsCreate(APICommentsCreateRequest apiRequest) {
        return commentsCreate(apiRequest.createCommentRequest());
    }

    /**
     * Get all comments
     */
    @GET
    @Path("/api/public/comments")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<GetCommentsResponse> commentsGet(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit,
            @QueryParam("objectType") String objectType,
            @QueryParam("objectId") String objectId,
            @QueryParam("authorUserId") String authorUserId);

    /**
     * Get all comments
     */
    @Override
    default CompletionStage<GetCommentsResponse> commentsGet(APICommentsGetRequest apiRequest) {
        return commentsGet(apiRequest.page(), apiRequest.limit(), apiRequest.objectType(), apiRequest.objectId(),
                apiRequest.authorUserId());
    }

    /**
     * Get a comment by id
     */
    @GET
    @Path("/api/public/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Comment> commentsGetById(
            @PathParam("commentId") String commentId);

    /**
     * Get a comment by id
     */
    @Override
    default CompletionStage<Comment> commentsGetById(APICommentsGetByIdRequest apiRequest) {
        return commentsGetById(apiRequest.commentId());
    }

}
