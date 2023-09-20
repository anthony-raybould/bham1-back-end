package org.kainos.ea.resources;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.checkerframework.checker.units.qual.A;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Api("Job Role API")
@Path("/api")
public class JobRoleController {
    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService) {
        Objects.requireNonNull(jobRoleService);

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

    @PUT
    @Path("/job-roles/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("Admin")
    @ApiOperation(value = "Edit a single job role", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION))
    // response = JobRoleResponse.class
    public Response editJobRole(@PathParam("id") @NotEmpty Short id, UpdateJobRoleRequest jobRoleToUpdate)
    {
        try{
            return Response.ok(jobRoleService.updateJobRole(id, jobRoleToUpdate)).build();
        }
        catch(FailedJobRolesOperationException | FailedToUpdateJobRoleException e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
