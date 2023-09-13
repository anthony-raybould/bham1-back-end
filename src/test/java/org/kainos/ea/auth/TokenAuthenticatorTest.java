package org.kainos.ea.auth;

import io.dropwizard.auth.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.User;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TokenAuthenticatorTest {

    AuthDao authDao = Mockito.mock(AuthDao.class);
    TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(authDao);

    @Test
    void tokenAuthenticator_shouldValidateLogin_whenValidToken() throws SQLException, AuthenticationException {
        Mockito.when(authDao.validateToken("validToken")).thenReturn(new User(1, "user@email.com", "Admin"));

        boolean hasUser = tokenAuthenticator.authenticate("validToken").isPresent();
        assertTrue(hasUser);

        User user = tokenAuthenticator.authenticate("validToken").get();
        assertEquals("user@email.com", user.getEmail());
    }


    @Test
    void tokenAuthenticator_shouldNotValidateLogin_whenInvalidToken() throws SQLException, AuthenticationException {
        Mockito.when(authDao.validateToken("invalidToken")).thenReturn(null);

        boolean hasUser = tokenAuthenticator.authenticate("invalidToken").isPresent();
        assertFalse(hasUser);
    }
}
