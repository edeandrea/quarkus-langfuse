package io.quarkiverse.langfuse.client.jaxrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.ResourceTypesResponse;
import com.langfuse.api.model.SchemasResponse;
import com.langfuse.api.model.ScimCreateUserRequest;
import com.langfuse.api.model.ScimUser;
import com.langfuse.api.model.ScimUsersListResponse;
import com.langfuse.api.model.ServiceProviderConfig;
import com.langfuse.api.scim.ScimApi.APIScimCreateUserRequest;
import com.langfuse.api.scim.ScimApi.APIScimDeleteUserRequest;
import com.langfuse.api.scim.ScimApi.APIScimGetUserRequest;
import com.langfuse.api.scim.ScimApi.APIScimListUsersRequest;

/**
 * Langfuse SCIM API
 */
public interface QuarkusScimApi extends com.langfuse.api.scim.ScimApi {

    /**
     * Create a new user in the organization
     */
    @POST
    @Path("/api/public/scim/Users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ScimUser scimCreateUser(
            ScimCreateUserRequest scimCreateUserRequest);

    /**
     * Create a new user in the organization
     */
    @Override
    default ScimUser scimCreateUser(APIScimCreateUserRequest apiRequest) {
        return scimCreateUser(apiRequest.scimCreateUserRequest());
    }

    /**
     * Remove a user from the organization
     */
    @DELETE
    @Path("/api/public/scim/Users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    Object scimDeleteUser(
            @PathParam("userId") String userId);

    /**
     * Remove a user from the organization
     */
    @Override
    default Object scimDeleteUser(APIScimDeleteUserRequest apiRequest) {
        return scimDeleteUser(apiRequest.userId());
    }

    /**
     * Get SCIM Resource Types
     */
    @GET
    @Path("/api/public/scim/ResourceTypes")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    ResourceTypesResponse scimGetResourceTypes();

    /**
     * Get SCIM Schemas
     */
    @GET
    @Path("/api/public/scim/Schemas")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    SchemasResponse scimGetSchemas();

    /**
     * Get SCIM Service Provider Configuration
     */
    @GET
    @Path("/api/public/scim/ServiceProviderConfig")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    ServiceProviderConfig scimGetServiceProviderConfig();

    /**
     * Get a specific user by ID
     */
    @GET
    @Path("/api/public/scim/Users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    ScimUser scimGetUser(
            @PathParam("userId") String userId);

    /**
     * Get a specific user by ID
     */
    @Override
    default ScimUser scimGetUser(APIScimGetUserRequest apiRequest) {
        return scimGetUser(apiRequest.userId());
    }

    /**
     * List users in the organization
     */
    @GET
    @Path("/api/public/scim/Users")
    @Produces(MediaType.APPLICATION_JSON)
    ScimUsersListResponse scimListUsers(
            @QueryParam("filter") String filter,
            @QueryParam("startIndex") Integer startIndex,
            @QueryParam("count") Integer count);

    /**
     * List users in the organization
     */
    @Override
    default ScimUsersListResponse scimListUsers(APIScimListUsersRequest apiRequest) {
        return scimListUsers(apiRequest.filter(), apiRequest.startIndex(), apiRequest.count());
    }

}
