package org.kainos.ea.api;

import org.kainos.ea.auth.TokenService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.cli.RegisterRequest;
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.SQLErrorCode;
import org.kainos.ea.client.*;
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.validator.RegisterValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class AuthService {

    private TokenService tokenService;
    private AuthDao authDao;
    private RegisterValidator registerValidator;

    public AuthService(AuthDao authDao, TokenService tokenService, RegisterValidator registerValidator) {
        Objects.requireNonNull(authDao);
        Objects.requireNonNull(tokenService);
        Objects.requireNonNull(registerValidator);

        this.authDao = authDao;
        this.tokenService = tokenService;
        this.registerValidator = registerValidator;
    }

    public String login(Login login) throws FailedToGenerateTokenException, FailedToGetUserPassword, FailedToGetUserId, FailedToLoginException {
        try {
            String userPassword = authDao.getUserPassword(login.getEmail());
            if (userPassword != null) {
                if (BCrypt.checkpw(login.getPassword(), userPassword))
                {
                    return tokenService.generateToken(login.getEmail());
                }
            }
            throw new FailedToGetUserPassword();
        } catch (SQLException e) {
            throw new FailedToGenerateTokenException();
        }
    }

    public void register(RegisterRequest request) throws FailedToRegisterException, DuplicateRegistrationException, FailedToValidateRegisterRequestException, ValidationFailedException {
        String validationError = registerValidator.validateRequest(request);
        if (validationError != null) {
            throw new ValidationFailedException(validationError);
        }

        String salt = BCrypt.gensalt(10);
        String hashedPassword = BCrypt.hashpw(request.getPassword(), salt);
        try {
            authDao.register(request.getEmail(), hashedPassword, request.getRole());
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLErrorCode.DUPLICATE_ENTRY.getErrorCode()) {
                throw new DuplicateRegistrationException();
            }
            throw new FailedToRegisterException();
        }
    }

    public List<Role> getRoles() throws FailedToGetRolesException {
        try {
            return authDao.getRoles();
        } catch (SQLException e) {
            throw new FailedToGetRolesException();
        }
    }

}
