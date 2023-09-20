package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;
import org.kainos.ea.client.FailedToValidateTokenException;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenAuthenticatorTest {

    TokenService tokenService = Mockito.mock(TokenService.class);
    TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(tokenService);

    @Test
    void tokenAuthenticator_shouldValidateLogin_whenValidToken() throws AuthenticationException, FailedToValidateTokenException {
        Mockito.when(tokenService.validateToken("validToken")).thenReturn(new User(1, "user@email.com", Role.ADMIN));

        boolean hasUser = tokenAuthenticator.authenticate("validToken").isPresent();
        assertTrue(hasUser);

        User user = tokenAuthenticator.authenticate("validToken").get();
        assertEquals("user@email.com", user.getEmail());
    }


    @Test
    void tokenAuthenticator_shouldNotValidateLogin_whenInvalidToken() throws SQLException, AuthenticationException, FailedToValidateTokenException {
        Mockito.when(tokenService.validateToken("invalidToken")).thenReturn(null);

        boolean hasUser = tokenAuthenticator.authenticate("invalidToken").isPresent();
        assertFalse(hasUser);
    }
}
