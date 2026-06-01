package io.quarkiverse.langfuse.client.jaxrs;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.DeleteTraceResponse;
import com.langfuse.api.model.TraceDeleteMultipleRequest;
import com.langfuse.api.model.TraceWithFullDetails;
import com.langfuse.api.model.Traces;
import com.langfuse.api.trace.TraceApi.APITraceDeleteMultipleRequest;
import com.langfuse.api.trace.TraceApi.APITraceDeleteRequest;
import com.langfuse.api.trace.TraceApi.APITraceGetRequest;
import com.langfuse.api.trace.TraceApi.APITraceListRequest;

/**
 * Langfuse Trace Async API
 */
public interface QuarkusTraceAsyncApi extends com.langfuse.api.trace.async.TraceApi {

    /**
     * Delete a specific trace
     */
    @DELETE
    @Path("/api/public/traces/{traceId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<DeleteTraceResponse> traceDelete(
            @PathParam("traceId") String traceId);

    /**
     * Delete a specific trace
     */
    @Override
    default CompletionStage<DeleteTraceResponse> traceDelete(APITraceDeleteRequest apiRequest) {
        return traceDelete(apiRequest.traceId());
    }

    /**
     * Delete multiple traces
     */
    @DELETE
    @Path("/api/public/traces")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<DeleteTraceResponse> traceDeleteMultiple(
            TraceDeleteMultipleRequest traceDeleteMultipleRequest);

    /**
     * Delete multiple traces
     */
    @Override
    default CompletionStage<DeleteTraceResponse> traceDeleteMultiple(APITraceDeleteMultipleRequest apiRequest) {
        return traceDeleteMultiple(apiRequest.traceDeleteMultipleRequest());
    }

    /**
     * Get a specific trace
     */
    @GET
    @Path("/api/public/traces/{traceId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<TraceWithFullDetails> traceGet(
            @PathParam("traceId") String traceId,
            @QueryParam("fields") String fields);

    /**
     * Get a specific trace
     */
    @Override
    default CompletionStage<TraceWithFullDetails> traceGet(APITraceGetRequest apiRequest) {
        return traceGet(apiRequest.traceId(), apiRequest.fields());
    }

    /**
     * Get list of traces
     */
    @GET
    @Path("/api/public/traces")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<Traces> traceList(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit,
            @QueryParam("userId") String userId,
            @QueryParam("name") String name,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("fromTimestamp") OffsetDateTime fromTimestamp,
            @QueryParam("toTimestamp") OffsetDateTime toTimestamp,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("tags") List<String> tags,
            @QueryParam("version") String version,
            @QueryParam("release") String release,
            @QueryParam("environment") List<String> environment,
            @QueryParam("fields") String fields,
            @QueryParam("filter") String filter);

    /**
     * Get list of traces
     */
    @Override
    default CompletionStage<Traces> traceList(APITraceListRequest apiRequest) {
        return traceList(apiRequest.page(), apiRequest.limit(), apiRequest.userId(), apiRequest.name(),
                apiRequest.sessionId(), apiRequest.fromTimestamp(), apiRequest.toTimestamp(), apiRequest.orderBy(),
                apiRequest.tags(), apiRequest.version(), apiRequest.release(), apiRequest.environment(),
                apiRequest.fields(), apiRequest.filter());
    }

}
