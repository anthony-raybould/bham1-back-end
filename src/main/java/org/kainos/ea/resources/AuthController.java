package org.kainos.ea.resources;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import org.kainos.ea.api.AuthService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.cli.User;
import org.kainos.ea.cli.UserResponse;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.client.FailedToGetUserPassword;
import org.kainos.ea.client.FailedToLoginException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Api("Authentication API")
@Path("/api")
@SwaggerDefinition(
    securityDefinition = @SecurityDefinition(
        apiKeyAuthDefinitions = {
            @ApiKeyAuthDefinition(
                key = HttpHeaders.AUTHORIZATION,
                name = HttpHeaders.AUTHORIZATION,
                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER
            )
        }
    )
)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        Objects.requireNonNull(authService);
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Logs in a user and returns a token", response = String.class)
    public Response login(Login login) {
        try {
            return Response.ok(authService.login(login)).build();
        } catch (FailedToLoginException | FailedToGetUserPassword e) {
            System.err.println(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (FailedToGenerateTokenException | FailedToGetUserId e) {
            System.err.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/whoami")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns the user that is currently logged in", response = UserResponse.class, authorizations = {
        @Authorization(value = HttpHeaders.AUTHORIZATION)
    })
    public Response whoami(@Auth @ApiParam(hidden = true) User user) {
        return Response.ok().entity(new UserResponse(user)).build();
    }
}
