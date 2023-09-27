package org.kainos.ea.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.CreateJobRoleRequest;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToCreateJobRoleRequestException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.client.InvalidJobRoleException;
import org.kainos.ea.client.UpdateJobRoleIDDoesNotExistException;
import org.kainos.ea.client.FailedToDeleteJobRoleException;
import org.kainos.ea.client.JobRoleDoesNotExistException;

import javax.annotation.security.RolesAllowed;
import javax.validation.ValidationException;
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

    @GET
    @Path("/job-roles/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Employee")
    @ApiOperation(value = "Returns a job role", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION))
    public Response getJobRoleById(@PathParam("id") int id) {
        try {
            return Response.ok(jobRoleService.getJobRoleById(id)).build();
        } catch (FailedJobRolesOperationException e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (JobRoleDoesNotExistException e) {
            System.err.println(e.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/job-roles/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    @ApiOperation(value = "Deletes a job role", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION))
    public Response deleteJobRoleById(@PathParam("id") int id) {
       try {
           return Response.ok(jobRoleService.deleteJobRole(id)).build();
       } catch (FailedToDeleteJobRoleException e) {
           e.printStackTrace();

           return Response.serverError().build();
       } catch (JobRoleDoesNotExistException e) {
           e.printStackTrace();

           return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
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
                return Response.ok(jobRoleService.updateJobRole(id, jobRoleToUpdate)).build();
        }
        catch(ValidationException | UpdateJobRoleIDDoesNotExistException e)
        {
            System.err.println(e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch(FailedJobRolesOperationException | FailedToUpdateJobRoleException e)
        {
            System.err.println(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/job-roles")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    @ApiOperation(value = "Create a job role", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION))
    public Response createJobRole(CreateJobRoleRequest jobRoleRequest) {
        try {
            return Response.ok(jobRoleService.createJobRole(jobRoleRequest)).build();
        } catch (FailedToCreateJobRoleRequestException e) {
            System.err.println(e.getMessage());

            return Response.serverError().build();
        } catch (InvalidJobRoleException e) {
            System.err.println(e.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
