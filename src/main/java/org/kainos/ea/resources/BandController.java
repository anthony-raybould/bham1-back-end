package org.kainos.ea.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cllient.FailedToGetBandsException;
import org.kainos.ea.api.BandService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Objects;

@Api("Job Band API")
@Path("/api")
public class BandController {
    private final BandService bandService;
    public BandController(BandService bandService) {
        Objects.requireNonNull(bandService);
        this.bandService = bandService;
    }

    @GET
    @Path("/band")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    @ApiOperation(value = "Returns all Capabilities", authorizations = @Authorization(value = HttpHeaders.AUTHORIZATION), response = JobBandResponse.class)
    public Response getJobRoles() {
        try {
            return Response.ok(bandService.getBands()).build();
        } catch (FailedToGetBandsException | SQLException e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
