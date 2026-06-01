package io.quarkiverse.langfuse.client.jaxrs;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.PaginatedSessions;
import com.langfuse.api.model.SessionWithTraces;
import com.langfuse.api.sessions.SessionsApi.APISessionsGetRequest;
import com.langfuse.api.sessions.SessionsApi.APISessionsListRequest;

/**
 * Langfuse Sessions Async API
 */
public interface QuarkusSessionsAsyncApi extends com.langfuse.api.sessions.async.SessionsApi {

    /**
     * Get a session
     */
    @GET
    @Path("/api/public/sessions/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<SessionWithTraces> sessionsGet(
            @PathParam("sessionId") String sessionId);

    /**
     * Get a session
     */
    @Override
    default CompletionStage<SessionWithTraces> sessionsGet(APISessionsGetRequest apiRequest) {
        return sessionsGet(apiRequest.sessionId());
    }

    /**
     * Get sessions
     */
    @GET
    @Path("/api/public/sessions")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<PaginatedSessions> sessionsList(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit,
            @QueryParam("fromTimestamp") OffsetDateTime fromTimestamp,
            @QueryParam("toTimestamp") OffsetDateTime toTimestamp,
            @QueryParam("environment") List<String> environment);

    /**
     * Get sessions
     */
    @Override
    default CompletionStage<PaginatedSessions> sessionsList(APISessionsListRequest apiRequest) {
        return sessionsList(apiRequest.page(), apiRequest.limit(), apiRequest.fromTimestamp(),
                apiRequest.toTimestamp(), apiRequest.environment());
    }

}
