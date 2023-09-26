package org.kainos.ea.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToDeleteCapabilityException;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.api.CapabilityService;
import org.kainos.ea.client.FailedToGetCapabilityReferences;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    @DELETE
    @Path("/capabilities/{capabilityID}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    @ApiOperation(value = "Deletes a capability", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION))
    public Response deleteCapabilities(@PathParam("capabilityID") short capabilityID) {
        try {
            ArrayList<Integer> references;
            references = capabilityService.getCapabilityReferences(capabilityID);
            if(Objects.requireNonNull(references).isEmpty())
            {
                return Response.ok(capabilityService.deleteCapability(capabilityID)).build();
            }
            return Response.status(409, "Tables reference the capability you wish to delete. Please delete the references before proceeding.").entity(references).build();
        } catch (FailedToDeleteCapabilityException | FailedToGetCapabilityReferences | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
