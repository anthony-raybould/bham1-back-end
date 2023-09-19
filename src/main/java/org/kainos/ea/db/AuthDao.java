package org.kainos.ea.db;

import org.kainos.ea.client.FailedToGetUserPassword;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class AuthDao {
    private DatabaseConnector databaseConnector;

    public AuthDao(DatabaseConnector databaseConnector) {
        Objects.requireNonNull(databaseConnector);
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
        } catch (SQLException e) {
            throw new FailedToGetUserPassword();
        }
        return null;
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
