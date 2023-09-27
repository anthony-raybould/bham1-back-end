package org.kainos.ea.integration.helpers;

import org.kainos.ea.auth.JWTService;
import org.kainos.ea.auth.TokenService;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.db.*;

import java.sql.SQLException;

public class AuthenticateUser {

    final DatabaseConnector databaseConnector = new DatabaseConnector();
    final AuthDao authDao = new AuthDao(databaseConnector);
    final JWTService jwtService = new JWTService();
    final TokenService tokenService = new TokenService(authDao, jwtService);

    public String loginAdmin() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        return tokenService.generateToken("admin@test.com");
    }
}
