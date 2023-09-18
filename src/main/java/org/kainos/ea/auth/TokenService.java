package org.kainos.ea.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.db.AuthDao;

import java.sql.SQLException;

public class TokenService {
    private AuthDao authDao;
    private JWTService jwtTokenService;


    public TokenService(AuthDao authDao, JWTService jwtTokenService)
    {
        if(authDao == null || jwtTokenService == null)
        {
            throw new NullPointerException("Null service passed into constructor.");
        }

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
}
