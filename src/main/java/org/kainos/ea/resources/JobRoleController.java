package org.kainos.ea.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.client.FailedJobRolesOperationException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api("Job Role API")
@Path("/api")
public class JobRoleController {
    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }

    @GET
    @Path("/job-roles")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Employee")
    @ApiOperation(value = "Returns all job roles", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION), response = JobRoleResponse.class)
    public Response getJobRoles() {
        try {
            return Response.ok(jobRoleService.getJobRoles()).build();
        } catch (FailedJobRolesOperationException e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
