package org.kainos.ea.api;

import org.kainos.ea.auth.TokenService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.client.FailedToGetUserPassword;
import org.kainos.ea.client.FailedToLoginException;
import org.kainos.ea.db.AuthDao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Objects;

public class AuthService {
    private TokenService tokenService;
    private AuthDao authDao;

    public AuthService(AuthDao authDao, TokenService tokenService) {
        Objects.requireNonNull(authDao);
        Objects.requireNonNull(tokenService);
        this.authDao = authDao;
        this.tokenService = tokenService;
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
}
