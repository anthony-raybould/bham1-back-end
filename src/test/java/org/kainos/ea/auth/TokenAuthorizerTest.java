package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.User;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenAuthorizerTest {

    TokenAuthorizer tokenAuthorizer = new TokenAuthorizer();

    @Test
    void tokenAuthorizer_shouldReturnTrue_whenUserHasRequiredRole() {
        String requiredRole = "Employee";

        User user = new User(1, "email", requiredRole);

        boolean isPermitted = tokenAuthorizer.authorize(user, requiredRole);

        assertTrue(isPermitted);
    }

    @Test
    void tokenAuthorizer_shouldReturnFalse_whenUserDoesNotHaveRequiredRole() {
        String requiredRole = "Employee";
        String userRole = "HR";

        User user = new User(1, "email", userRole);

        boolean isPermitted = tokenAuthorizer.authorize(user, requiredRole);

        assertFalse(isPermitted);
    }

    @Test
    void tokenAuthorizer_shouldReturnTrue_whenUserIsAdmin() {
        String requiredRole = "Employee";

        User user = new User(1, "email", "Admin");

        boolean isPermitted = tokenAuthorizer.authorize(user, requiredRole);

        assertTrue(isPermitted);
    }
}
