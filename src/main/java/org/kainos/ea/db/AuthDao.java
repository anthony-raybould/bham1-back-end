package org.kainos.ea.db;

import org.apache.commons.lang3.time.DateUtils;
import org.kainos.ea.cli.Login;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class AuthDao {
    private DatabaseConnector databaseConnector;

    public AuthDao(DatabaseConnector databaseConnector) {
        if(databaseConnector == null)
        {
            throw new NullPointerException("Database connector passed in ctor is null");
        }
        this.databaseConnector = databaseConnector;
    }

    public boolean validLogin(Login login) {
        try (Connection c = databaseConnector.getConnection()) {
            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT Password FROM `User` WHERE Email = '"
                    + login.getEmail() + "'");

            while (rs.next()) {
                return rs.getString("password").equals(login.getPassword());
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public String generateToken(String username) throws SQLException {
        String token = UUID.randomUUID().toString();
        Date expiry = DateUtils.addHours(new Date(), 24);

        Connection c = databaseConnector.getConnection();

        String insertStatement = "INSERT INTO Token (Email, Token, Expiry) " +
                "VALUES (?, ?, ?)";

        PreparedStatement st = c.prepareStatement(insertStatement);

        st.setString(1, username);
        st.setString(2, token);
        st.setTimestamp(3, new java.sql.Timestamp(expiry.getTime()));

        st.executeUpdate();

        return token;
    }
}
