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
import org.kainos.ea.validator.UpdateJobRoleValidator;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Api("Job Role API")
@Path("/api")
public class JobRoleController {
    private final JobRoleService jobRoleService;
    private final UpdateJobRoleValidator jobRoleValidator;

    public JobRoleController(JobRoleService jobRoleService, UpdateJobRoleValidator jobRoleValidator) {
        Objects.requireNonNull(jobRoleService);
        Objects.requireNonNull(jobRoleValidator);

        this.jobRoleValidator = jobRoleValidator;
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
    @RolesAllowed("Admin")
    @ApiOperation(value = "Edit a single job role", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION))
    public Response editJobRole(@PathParam("id") @NotNull Short id, UpdateJobRoleRequest jobRoleToUpdate)
    {
        try{
            if(jobRoleValidator.validate(jobRoleToUpdate))
            {
                return Response.ok(jobRoleService.updateJobRole(id, jobRoleToUpdate)).build();
            }
            return Response.status(400, "Fields not in correct format.").build();
        }
        catch(FailedJobRolesOperationException | FailedToUpdateJobRoleException e)
        {
            System.err.println(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
