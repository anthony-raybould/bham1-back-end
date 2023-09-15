package org.kainos.ea.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.api.AuthService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.client.FailedToGetUserPassword;
import org.kainos.ea.client.FailedToLoginException;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    AuthDao authDao = Mockito.mock(AuthDao.class);
    AuthService authService = new AuthService(authDao);
    String hashedPassword = "$2a$12$B461LMSk1z/NobdooXnvjOACFh2TLZ0jgYcqO2ZcC0egW75fEeW/.";
    Login userLogin = new Login(
            "email@email.com",
            "password"
    );
    @Test
    void constructor_shouldThrowNullPointerException_whenNullService() throws NullPointerException {
        AuthDao nullAuthDao = null;

        assertThrows(NullPointerException.class,
                () -> new AuthService(nullAuthDao));
    }

    @Test
    void login_shouldThrowFailedToGetUserPassword_whenInvalidEmail() throws FailedToGetUserPassword {
        Mockito.when(authDao.getUserPassword(any(String.class))).thenReturn(null);

        assertThrows(FailedToGetUserPassword.class,
                () -> authService.login(userLogin));
    }


    @Test
    void login_shouldThrowFailedToLoginException_whenSQLExceptionThrown() throws SQLException, FailedToGetUserId, FailedToGetUserPassword, FailedToGenerateTokenException {
        Mockito.when(authDao.getUserPassword((any(String.class)))).thenReturn(hashedPassword);
        Mockito.when(authDao.generateToken(any(String.class))).thenThrow(SQLException.class);

        assertThrows(FailedToGenerateTokenException.class,
                () -> authService.login(userLogin));
    }

    @Test
    void login_shouldReturnToken_whenValidLogin() throws SQLException, FailedToLoginException, FailedToGenerateTokenException, FailedToGetUserId, FailedToGetUserPassword {
        Mockito.when(authDao.getUserPassword((any(String.class)))).thenReturn(hashedPassword);
        Mockito.when(authDao.generateToken(any(String.class))).thenReturn("tokenString");
        userLogin.setPassword("password");

        String result = authService.login(userLogin);
        assertEquals(result, "tokenString");
    }
    @Test
    void login_shouldThrowFailedToGetUserPassword_whenGetUserPasswordReturnNull() throws FailedToGetUserPassword, FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        Mockito.when(authDao.getUserPassword((any(String.class)))).thenReturn(null );
        Mockito.when(authDao.generateToken(any(String.class))).thenReturn("tokenString");

        assertThrows(FailedToGetUserPassword.class,
        () -> authService.login(userLogin));
    }
}

