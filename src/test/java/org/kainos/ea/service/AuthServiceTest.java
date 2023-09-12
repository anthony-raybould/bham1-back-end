package org.kainos.ea.service;

import io.dropwizard.auth.Auth;
import org.eclipse.jetty.util.security.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.api.AuthService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToLoginException;
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.db.DatabaseConnector;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.Email;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    AuthDao authDao = Mockito.mock(AuthDao.class);
    AuthService authService = new AuthService(authDao);
    Login userLogin = new Login(
            "email@email.com",
            "thisIsABadPassword"
    );
    @Test
    void ctor_shouldThrowNullPointerException_whenNullService() throws NullPointerException {
        AuthDao nullAuthDao = null;

        assertThrows(NullPointerException.class,
                () -> new AuthService(nullAuthDao));
    }

    @Test
    void login_shouldThrowFailedToLoginException_whenInvalidLogin() throws FailedToLoginException{
        Mockito.when(authDao.validLogin(any(Login.class))).thenReturn(false);

        assertThrows(FailedToLoginException.class,
                () -> authService.login(userLogin));
    }

    @Test
    void login_shouldThrowFailedToGenerateLoginToken_whenAuthDaoThrowSQLException() throws SQLException, FailedToGenerateTokenException{
        Mockito.when(authDao.validLogin((any(Login.class)))).thenReturn(true);
        Mockito.when(authDao.generateToken(any(String.class))).thenThrow(SQLException.class);

        assertThrows(FailedToGenerateTokenException.class,
                () -> authService.login(userLogin));
    }

    @Test
    void login_shouldReturnToken_whenValidLogin() throws SQLException, FailedToLoginException, FailedToGenerateTokenException {
        Mockito.when(authDao.validLogin((any(Login.class)))).thenReturn(true);
        Mockito.when(authDao.generateToken(any(String.class))).thenReturn("tokenString");

        String result = authService.login(userLogin);
        assertEquals(result, "tokenString");
    }
}

