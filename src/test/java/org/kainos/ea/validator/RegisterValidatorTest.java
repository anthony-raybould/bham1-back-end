package org.kainos.ea.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.Role;
import org.kainos.ea.client.FailedToValidateRegisterRequestException;
import org.kainos.ea.db.AuthDao;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

public class RegisterValidatorTest {

    private static final String VALID_EMAIL = "test@test.com";
    private static final String VALID_PASSWORD = "Test123!";
    private static final String FAILED_PASSWORD_REGEX_ERROR = "Password must contain at least one uppercase letter, one " +
            "lowercase letter, one number, and one special character";


    private RegisterValidator registerValidator;
    private AuthDao authDao;

    @BeforeEach
    public void setUp() {
        authDao = Mockito.mock(AuthDao.class);
        registerValidator = new RegisterValidator(authDao);
    }

    @Test
    public void validateRequest_shouldCallOtherValidationMethods() throws FailedToValidateRegisterRequestException, SQLException {
        RegisterValidator registerValidatorSpy = Mockito.spy(registerValidator);
        Mockito.when(authDao.getRole(1)).thenReturn(new Role(1, "role"));

        registerValidatorSpy.validateRequest(new org.kainos.ea.cli.RegisterRequest(VALID_EMAIL, VALID_PASSWORD, 1));

        Mockito.verify(registerValidatorSpy).validateEmail(VALID_EMAIL);
        Mockito.verify(registerValidatorSpy).validatePassword(VALID_PASSWORD);
        Mockito.verify(registerValidatorSpy).validateRole(1);
    }

    @Test
    public void validateEmail_shouldReturnNull_whenEmailIsValid() {
        String result = registerValidator.validateEmail(VALID_EMAIL);
        assertNull(result);
    }

    @Test
    public void validateEmail_shouldReturnError_whenEmailIsInvalid() {
        String result = registerValidator.validateEmail("test");
        assertEquals(result, "Email must be valid");
    }

    @Test
    public void validatePassword_shouldReturnNull_whenPasswordIsValid() {
        String result = registerValidator.validatePassword(VALID_PASSWORD);
        assertNull(result);
    }

    @Test
    public void validatePassword_shouldReturnError_whenPasswordIsTooShort() {
        String result = registerValidator.validatePassword("test");
        assertEquals(result, "Password must be at least 8 characters");
    }

    @Test
    public void validatePassword_shouldReturnError_whenPasswordDoesNotContainUppercase() {
        String result = registerValidator.validatePassword("test123!");
        assertEquals(result, FAILED_PASSWORD_REGEX_ERROR);
    }

    @Test
    public void validatePassword_shouldReturnError_whenPasswordDoesNotContainLowercase() {
        String result = registerValidator.validatePassword("TEST123!");
        assertEquals(result, FAILED_PASSWORD_REGEX_ERROR);
    }

    @Test
    public void validatePassword_shouldReturnError_whenPasswordDoesNotContainNumber() {
        String result = registerValidator.validatePassword("TestTest!");
        assertEquals(result, FAILED_PASSWORD_REGEX_ERROR);
    }

    @Test
    public void validatePassword_shouldReturnError_whenPasswordDoesNotContainSpecialCharacter() {
        String result = registerValidator.validatePassword("TestTest1");
        assertEquals(result, FAILED_PASSWORD_REGEX_ERROR);
    }

    @Test
    public void validateRole_shouldReturnNull_whenRoleIsValid() throws FailedToValidateRegisterRequestException, SQLException {
        Mockito.when(authDao.getRole(1)).thenReturn(new Role(1, "role"));
        String result = registerValidator.validateRole(1);
        assertNull(result);
    }

    @Test
    public void validateRole_shouldReturnError_whenRoleIsInvalid() throws FailedToValidateRegisterRequestException, SQLException {
        Mockito.when(authDao.getRole(any())).thenReturn(null);
        String result = registerValidator.validateRole(1);
        assertEquals(result, "Role must be valid");
    }

}
