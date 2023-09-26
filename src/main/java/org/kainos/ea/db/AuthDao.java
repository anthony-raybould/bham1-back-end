package org.kainos.ea.db;

import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;
import org.kainos.ea.client.FailedToGetUserPassword;

import java.sql.*;
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

    public User getUser(int userId) throws SQLException {
        Connection c = databaseConnector.getConnection();

        PreparedStatement ps = c.prepareStatement("SELECT userID, email, Role.roleID, Role.name FROM `User`" +
                " JOIN Role ON User.roleID = Role.roleID" +
                " WHERE userID = ?");

        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getInt("userID"),
                    rs.getString("email"),
                    new Role(
                            rs.getInt("roleID"),
                            rs.getString("name")
                    )
            );
        }

        return null;
    }
}
