package org.kainos.ea.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.api.AuthService;
import org.kainos.ea.auth.TokenService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.cli.RegisterRequest;
import org.kainos.ea.cli.Role;
import org.kainos.ea.client.*;
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.validator.RegisterValidator;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    AuthDao authDao = Mockito.mock(AuthDao.class);
    TokenService tokenService = Mockito.mock(TokenService.class);
    RegisterValidator registerValidator = Mockito.mock(RegisterValidator.class);
    AuthService authService = new AuthService(authDao, tokenService, registerValidator);
    String hashedPassword = "$2a$12$B461LMSk1z/NobdooXnvjOACFh2TLZ0jgYcqO2ZcC0egW75fEeW/.";
    Login userLogin = new Login(
            "email@email.com",
            "password"
    );


    @Test
    void constructor_shouldThrowNullPointerException_whenNullDaoService() throws NullPointerException {
        AuthDao nullAuthDao = null;

        assertThrows(NullPointerException.class,
                () -> new AuthService(nullAuthDao, tokenService, registerValidator));
    }
    @Test
    void constructor_shouldThrowNullPointerException_whenNullTokenService() throws NullPointerException {
        TokenService nullService = null;

        assertThrows(NullPointerException.class,
                () -> new AuthService(authDao, nullService, registerValidator));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenNullRegisterValidator() throws NullPointerException {
        RegisterValidator nullRegisterValidator = null;

        assertThrows(NullPointerException.class,
                () -> new AuthService(authDao, tokenService, nullRegisterValidator));
    }

    @Test
    void login_shouldThrowFailedToGetUserPassword_whenInvalidEmail() throws FailedToGetUserPassword {
        Mockito.when(authDao.getUserPassword(any(String.class))).thenReturn(null);

        assertThrows(FailedToGetUserPassword.class,
                () -> authService.login(userLogin));
    }

    @Test
    void login_shouldThrowFailedToLoginException_whenSQLExceptionThrown() throws SQLException, FailedToGetUserId,
            FailedToGetUserPassword, FailedToGenerateTokenException {
        Mockito.when(authDao.getUserPassword((any(String.class)))).thenReturn(hashedPassword);
        Mockito.when(tokenService.generateToken(any(String.class))).thenThrow(SQLException.class);

        assertThrows(FailedToGenerateTokenException.class,
                () -> authService.login(userLogin));
    }

    @Test
    void login_shouldReturnToken_whenValidLogin() throws SQLException, FailedToLoginException,
            FailedToGenerateTokenException, FailedToGetUserId, FailedToGetUserPassword, InterruptedException {
        Mockito.when(authDao.getUserPassword(any(String.class))).thenReturn(hashedPassword);
        Mockito.when(tokenService.generateToken(any(String.class))).thenReturn("tokenString");

        userLogin.setPassword("password");
        String result = authService.login(userLogin);
        assertEquals("tokenString", result);
    }
    @Test
    void login_shouldThrowFailedToGetUserPassword_whenGetUserPasswordReturnNull() throws FailedToGetUserPassword,
            FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        Mockito.when(authDao.getUserPassword((any(String.class)))).thenReturn(null );

        assertThrows(FailedToGetUserPassword.class,
                () -> authService.login(userLogin));
    }

    @Test
    void register_shouldThrowFailedToRegisterException_whenSQLExceptionThrown() throws SQLException,
            FailedToValidateRegisterRequestException {
        Mockito.when(registerValidator.validateRequest(any(RegisterRequest.class))).thenReturn(null);
        Mockito.when(authDao.register(any(String.class), any(String.class), any(int.class))).thenThrow(SQLException.class);

        assertThrows(FailedToRegisterException.class,
                () -> authService.register(new RegisterRequest("email", "password", 1)));
    }

    @Test
    void register_shouldThrowDuplicateRegistrationException_whenDuplicateRegistration() throws SQLException,
            FailedToValidateRegisterRequestException {
        Mockito.when(registerValidator.validateRequest(any(RegisterRequest.class))).thenReturn(null);
        Mockito.when(authDao.register(any(String.class), any(String.class), any(int.class))).thenThrow(
                new SQLException("Duplicate entry 'email' for key 'email'", "23000", 1062, null));

        assertThrows(DuplicateRegistrationException.class,
                () -> authService.register(new RegisterRequest("email", "password", 1)));
    }

    @Test
    void register_shouldThrowValidationFailedException_whenValidationFails() throws SQLException,
            FailedToValidateRegisterRequestException {
        Mockito.when(registerValidator.validateRequest(any(RegisterRequest.class))).thenReturn("error");

        assertThrows(ValidationFailedException.class,
                () -> authService.register(new RegisterRequest("email", "password", 1)));
    }

    @Test
    void register_shouldReturn_whenValid() throws SQLException, FailedToRegisterException, ValidationFailedException,
            FailedToValidateRegisterRequestException {
        Mockito.when(registerValidator.validateRequest(any(RegisterRequest.class))).thenReturn(null);
        Mockito.when(authDao.register(any(String.class), any(String.class), any(int.class))).thenReturn(true);

        authService.register(new RegisterRequest("email", "password", 1));
    }


    @Test
    void getRoles_shouldThrowFailedToGetRolesException_whenSQLExceptionThrown() throws SQLException {
        Mockito.when(authDao.getRoles()).thenThrow(SQLException.class);

        assertThrows(FailedToGetRolesException.class,
                () -> authService.getRoles());
    }

    @Test
    void getRoles_shouldReturn_whenValid() throws SQLException, FailedToGetRolesException {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "Admin"));
        roles.add(new Role(2, "Employee"));

        Mockito.when(authDao.getRoles()).thenReturn(roles);

        List<Role> returnedRoles = authService.getRoles();

        assertEquals(roles, returnedRoles);
    }
}

