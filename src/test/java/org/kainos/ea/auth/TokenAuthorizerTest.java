package org.kainos.ea.auth;

import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
