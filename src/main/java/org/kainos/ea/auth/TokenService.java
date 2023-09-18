package org.kainos.ea.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.apache.commons.lang3.time.DateUtils;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.db.AuthDao;

import java.sql.SQLException;
import java.util.Date;

public class TokenService {
    private AuthDao authDao;

    public TokenService(AuthDao authDao)
    {
        if(authDao == null)
        {
            throw new NullPointerException("Auth dao service passed in is null.");
        }
        this.authDao = authDao;
    }
    public String generateToken(String email) throws SQLException, FailedToGetUserId, FailedToGenerateTokenException {
        int userId = authDao.getUserId(email);
        if (userId == -1) {
            throw new FailedToGetUserId();
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256("superSecretWords");

            return JWT.create().withExpiresAt(DateUtils.addDays(new Date(), 1))
                    .withClaim("userId", userId)
                    .withClaim("email", email)
                    .withIssuer("auth0")
                    .sign(algorithm);
        }
        catch (JWTCreationException exception) {
            throw new FailedToGenerateTokenException();
        }
    }
}
