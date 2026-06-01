package io.quarkiverse.langfuse.client.jaxrs;

import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.langfuse.api.model.DeleteMembershipRequest;
import com.langfuse.api.model.MembershipDeletionResponse;
import com.langfuse.api.model.MembershipRequest;
import com.langfuse.api.model.MembershipResponse;
import com.langfuse.api.model.MembershipsResponse;
import com.langfuse.api.model.OrganizationApiKeysResponse;
import com.langfuse.api.model.OrganizationProjectsResponse;
import com.langfuse.api.organizations.OrganizationsApi.APIOrganizationsDeleteOrganizationMembershipRequest;
import com.langfuse.api.organizations.OrganizationsApi.APIOrganizationsDeleteProjectMembershipRequest;
import com.langfuse.api.organizations.OrganizationsApi.APIOrganizationsGetProjectMembershipsRequest;
import com.langfuse.api.organizations.OrganizationsApi.APIOrganizationsUpdateOrganizationMembershipRequest;
import com.langfuse.api.organizations.OrganizationsApi.APIOrganizationsUpdateProjectMembershipRequest;

public interface QuarkusOrganizationsAsyncApi extends com.langfuse.api.organizations.async.OrganizationsApi {

    /**
     * Delete a membership from the organization associated with the API key
     */
    @DELETE
    @Path("/api/public/organizations/memberships")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<MembershipDeletionResponse> organizationsDeleteOrganizationMembership(
            DeleteMembershipRequest deleteMembershipRequest);

    /**
     * Delete a membership from the organization associated with the API key
     */
    @Override
    default CompletionStage<MembershipDeletionResponse> organizationsDeleteOrganizationMembership(
            APIOrganizationsDeleteOrganizationMembershipRequest apiRequest) {
        return organizationsDeleteOrganizationMembership(apiRequest.deleteMembershipRequest());
    }

    /**
     * Delete a membership from a specific project
     */
    @DELETE
    @Path("/api/public/projects/{projectId}/memberships")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<MembershipDeletionResponse> organizationsDeleteProjectMembership(
            @PathParam("projectId") String projectId,
            DeleteMembershipRequest deleteMembershipRequest);

    /**
     * Delete a membership from a specific project
     */
    @Override
    default CompletionStage<MembershipDeletionResponse> organizationsDeleteProjectMembership(
            APIOrganizationsDeleteProjectMembershipRequest apiRequest) {
        return organizationsDeleteProjectMembership(apiRequest.projectId(), apiRequest.deleteMembershipRequest());
    }

    /**
     * Get all API keys for the organization associated with the API key
     */
    @GET
    @Path("/api/public/organizations/apiKeys")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    CompletionStage<OrganizationApiKeysResponse> organizationsGetOrganizationApiKeys();

    /**
     * Get all memberships for the organization associated with the API key
     */
    @GET
    @Path("/api/public/organizations/memberships")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    CompletionStage<MembershipsResponse> organizationsGetOrganizationMemberships();

    /**
     * Get all projects for the organization associated with the API key
     */
    @GET
    @Path("/api/public/organizations/projects")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    CompletionStage<OrganizationProjectsResponse> organizationsGetOrganizationProjects();

    /**
     * Get all memberships for a specific project
     */
    @GET
    @Path("/api/public/projects/{projectId}/memberships")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<MembershipsResponse> organizationsGetProjectMemberships(
            @PathParam("projectId") String projectId);

    /**
     * Get all memberships for a specific project
     */
    @Override
    default CompletionStage<MembershipsResponse> organizationsGetProjectMemberships(
            APIOrganizationsGetProjectMembershipsRequest apiRequest) {
        return organizationsGetProjectMemberships(apiRequest.projectId());
    }

    /**
     * Create or update a membership for the organization associated with the API key
     */
    @PUT
    @Path("/api/public/organizations/memberships")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<MembershipResponse> organizationsUpdateOrganizationMembership(
            MembershipRequest membershipRequest);

    /**
     * Create or update a membership for the organization associated with the API key
     */
    @Override
    default CompletionStage<MembershipResponse> organizationsUpdateOrganizationMembership(
            APIOrganizationsUpdateOrganizationMembershipRequest apiRequest) {
        return organizationsUpdateOrganizationMembership(apiRequest.membershipRequest());
    }

    /**
     * Create or update a membership for a specific project
     */
    @PUT
    @Path("/api/public/projects/{projectId}/memberships")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<MembershipResponse> organizationsUpdateProjectMembership(
            @PathParam("projectId") String projectId,
            MembershipRequest membershipRequest);

    /**
     * Create or update a membership for a specific project
     */
    @Override
    default CompletionStage<MembershipResponse> organizationsUpdateProjectMembership(
            APIOrganizationsUpdateProjectMembershipRequest apiRequest) {
        return organizationsUpdateProjectMembership(apiRequest.projectId(), apiRequest.membershipRequest());
    }

}
