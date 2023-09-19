package org.kainos.ea.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.kainos.ea.cli.User;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.client.FailedToValidateTokenException;
import org.kainos.ea.db.AuthDao;

import java.sql.SQLException;
import java.util.Objects;

public class TokenService {
    private AuthDao authDao;
    private JWTService jwtTokenService;

    public TokenService(AuthDao authDao, JWTService jwtTokenService)
    {
        Objects.requireNonNull(authDao);
        Objects.requireNonNull(jwtTokenService);
        this.authDao = authDao;
        this.jwtTokenService = jwtTokenService;
    }

    public String generateToken(String email) throws SQLException, FailedToGetUserId, FailedToGenerateTokenException {
        int userId = authDao.getUserId(email);
        if (userId == -1) {
            throw new FailedToGetUserId();
        }

        try {
            return jwtTokenService.create(email,userId);
        }
        catch (JWTCreationException exception) {
            throw new FailedToGenerateTokenException();
        }
    }

    public User validateToken(String token) throws FailedToValidateTokenException {
        try {
            int userId = jwtTokenService.verify(token);
            return authDao.getUser(userId);
        } catch (JWTVerificationException | SQLException exception) {
            throw new FailedToValidateTokenException();
        }
    }

}
