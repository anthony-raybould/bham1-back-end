package org.kainos.ea.validator;

import org.kainos.ea.cli.RegisterRequest;
import org.kainos.ea.client.FailedToValidateRegisterRequestException;
import org.kainos.ea.db.AuthDao;

import java.util.regex.Pattern;

public class RegisterValidator {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");

    private final AuthDao authDao;

    public RegisterValidator(AuthDao authDao) {
        this.authDao = authDao;
    }

    public String validateRequest(RegisterRequest request) throws FailedToValidateRegisterRequestException {
        String emailError = validateEmail(request.getEmail());
        if (emailError != null) {
            return emailError;
        }
        String passwordError = validatePassword(request.getPassword());
        if (passwordError != null) {
            return passwordError;
        }
        return validateRole(request.getRole());
    }

    public String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email is required";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Email must be valid";
        }
        return null;
    }

    public String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters";
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";
        }
        return null;
    }

    public String validateRole(int roleId) throws FailedToValidateRegisterRequestException {
        try {
            if (authDao.getRole(roleId) == null) {
                return "Role must be valid";
            }
        } catch (Exception e) {
            throw new FailedToValidateRegisterRequestException(e);
        }
        return null;
    }

}
