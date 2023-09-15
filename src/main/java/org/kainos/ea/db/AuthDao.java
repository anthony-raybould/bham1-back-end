package org.kainos.ea.db;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.jetty.server.Authentication;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.client.FailedToGetUserPassword;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class AuthDao {
    private DatabaseConnector databaseConnector;

    public AuthDao(DatabaseConnector databaseConnector) {
        if (databaseConnector == null) {
            throw new NullPointerException("Database connector passed into constructor is null");
        }
        this.databaseConnector = databaseConnector;
    }

    public String getUserPassword(String userEmail) throws FailedToGetUserPassword {
        try (Connection c = databaseConnector.getConnection()) {
            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT password FROM `User` WHERE email = '"
                    + userEmail + "'");
            while (rs.next()) {
                return rs.getString("password");
            }
        }
        catch (SQLException e)
        {
            throw new FailedToGetUserPassword();
        }
        return null;
    }
    public String generateToken(String email) throws SQLException, FailedToGetUserId, FailedToGenerateTokenException {
        int userId = getUserId(email);
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

    public int getUserId(String email) throws SQLException {
        Connection c = databaseConnector.getConnection();
        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery("SELECT userId FROM `User` WHERE email = '"
                + email + "'");

        while (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }
}
