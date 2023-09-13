package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;
import java.security.Principal;
import java.util.Optional;

public class TokenAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return "Admin".equals(user.getRole().toString()) || role.equals(user.getRole().toString());
    }

}
