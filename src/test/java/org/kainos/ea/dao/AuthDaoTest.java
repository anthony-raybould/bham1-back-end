package org.kainos.ea.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.auth.JWTService;
import org.kainos.ea.auth.TokenService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;
import org.kainos.ea.client.FailedToGetUserPassword;
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.db.DatabaseConnector;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AuthDaoTest {
    DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
    AuthDao authDao = new AuthDao(databaseConnector);
    JWTService jwtService = new JWTService();
    TokenService tokenService = new TokenService(authDao,jwtService);
    String hashedPassword = BCrypt.hashpw("mySecurePassword", BCrypt.gensalt());
    Login userLogin = new Login(
            "email@email.com",
            hashedPassword
    );

    @Test
    public void constructor_shouldThrowNullPointerException_whenNullServiceInConstructor() throws NullPointerException {
        DatabaseConnector nullDatabaseConnector = null;

        assertThrows(NullPointerException.class,
                () -> new AuthDao(nullDatabaseConnector));
    }

    @Test
    public void getUserPassword_shouldThrowFailedToGetUserPassword_whenGetConnectionThrowSqlException() throws SQLException {
        Mockito.when(databaseConnector.getConnection()).thenThrow(SQLException.class);
        assertThrows(FailedToGetUserPassword.class,
                () -> authDao.getUserPassword(userLogin.getEmail())
        );
    }

    @Test
    public void getUserId_shouldReturnNegative1_whenCantFindId() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        int result = authDao.getUserId("email@email.com");
        assertEquals(result, -1);
    }

    @Test
    public void getUserId_shouldReturnUserId_whenValid() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true);
        Mockito.when(resultSetMock.getInt(1)).thenReturn(1);

        int result = authDao.getUserId("email@email.com");
        assertEquals(result, 1);
    }

    @Test
    public void getRoles_shouldReturnListOfRoles_whenValid() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getInt("roleID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("name")).thenReturn("Admin");

        List<Role> roles = authDao.getRoles();

        assertEquals(roles.size(), 1);
        assertEquals(roles.get(0).getRoleId(), 1);
        assertEquals(roles.get(0).getRoleName(), "Admin");
    }

    public void getUser_shouldReturnUser_whenValid() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getInt("userID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("email")).thenReturn("email");
        Mockito.when(resultSetMock.getInt("roleID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("name")).thenReturn("Admin");

        User user = authDao.getUser(1);

        assertEquals(user.getEmail(), "email");
        assertEquals(user.getId(), 1);
        assertEquals(user.getRole().getRoleId(), 1);
        assertEquals(user.getRole().getRoleName(), "Admin");
    }

    @Test
    public void getUser_shouldReturnNull_whenInvalid() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        User user = authDao.getUser(1);

        assertNull(user);
    }
}
