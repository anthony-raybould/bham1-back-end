package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenAuthorizerTest {

    TokenAuthorizer tokenAuthorizer = new TokenAuthorizer();

    @Test
    void tokenAuthorizer_shouldReturnTrue_whenUserHasRequiredRole() {
        Role requiredRole = Role.EMPLOYEE;

        User user = new User(1, "email", requiredRole);

        boolean isPermitted = tokenAuthorizer.authorize(user, requiredRole.toString());

        assertTrue(isPermitted);
    }

    @Test
    void tokenAuthorizer_shouldReturnFalse_whenUserDoesNotHaveRequiredRole() {
        Role requiredRole = Role.ADMIN;
        Role userRole = Role.EMPLOYEE;

        User user = new User(1, "email", userRole);

        boolean isPermitted = tokenAuthorizer.authorize(user, requiredRole.toString());

        assertFalse(isPermitted);
    }

    @Test
    void tokenAuthorizer_shouldReturnTrue_whenUserIsAdmin() {
        Role requiredRole = Role.ADMIN;

        User user = new User(1, "email", requiredRole);

        boolean isPermitted = tokenAuthorizer.authorize(user, requiredRole.toString());

        assertTrue(isPermitted);
    }
}
