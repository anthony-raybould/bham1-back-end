package org.kainos.ea.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.kainos.ea.cli.CreateCapabilityRequest;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToCreateCapabilityException;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.api.CapabilityService;

import javax.annotation.security.RolesAllowed;
import javax.validation.ValidationException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Objects;

@Api("Job Capability API")
@Path("/api")
public class CapabilityController {
    private final CapabilityService capabilityService;
    public CapabilityController(CapabilityService capabilityService) {
        Objects.requireNonNull(capabilityService);
        this.capabilityService = capabilityService;
    }

    @GET
    @Path("/capabilities")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    @ApiOperation(value = "Returns all Capabilities", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION), response = JobCapabilityResponse.class)
    public Response getJobRoles() {
        try {
            return Response.ok(capabilityService.getCapabilities()).build();
        } catch (FailedToGetCapabilitiesException e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/capabilities")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    @ApiOperation(value = "Creates a capability", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION), response = Integer.class)
    public Response createCapability(CreateCapabilityRequest request) {

        try {
            return Response.ok(capabilityService.createCapability(request)).build();

        } catch (FailedToCreateCapabilityException e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
