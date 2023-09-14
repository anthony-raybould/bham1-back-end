package org.kainos.ea.db;

import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.jetty.server.Authentication;
import org.kainos.ea.cli.Login;
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
    public String generateToken(String email) throws SQLException, FailedToGetUserId {
        int userId = getUserId(email);
        if (userId == -1) {
            throw new FailedToGetUserId();
        }

        String token = UUID.randomUUID().toString();
        Date expiry = DateUtils.addHours(new Date(), 24);

        Connection c = databaseConnector.getConnection();

        String insertStatement = "INSERT INTO Token (userID, token, expiry) " +
                "VALUES (?, ?, ?)";

        PreparedStatement st = c.prepareStatement(insertStatement);

        st.setInt(1, userId);
        st.setString(2, token);
        st.setTimestamp(3, new java.sql.Timestamp(expiry.getTime()));

        st.executeUpdate();

        return token;
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
