package org.kainos.ea.dao;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.auth.JWTService;
import org.kainos.ea.auth.TokenService;
import org.kainos.ea.cli.Login;
<<<<<<< HEAD
import org.kainos.ea.cli.Role;
import org.kainos.ea.cli.User;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToLoginException;
=======
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.client.FailedToGetUserPassword;
>>>>>>> 040-login-system
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.db.DatabaseConnector;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void validateToken_shouldReturnUser_whenValidToken() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Date futureExpiryDate = DateUtils.addHours(new Date(), 24);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getTimestamp("expiry")).thenReturn(new java.sql.Timestamp(futureExpiryDate.getTime()));
        Mockito.when(resultSetMock.getInt("userID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("email")).thenReturn("user@email.com");
        Mockito.when(resultSetMock.getInt("roleID")).thenReturn(1);

        User result = authDao.validateToken("validToken");

        assertEquals(1, result.getId());
        assertEquals("user@email.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void validateToken_shouldNotReturnUser_whenInvalidToken() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        User result = authDao.validateToken("invalidToken");

        assertNull(result);
    }

    @Test
    void validateToken_shouldNotReturnUser_whenExpiredToken() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Date pastExpiryDate = DateUtils.addHours(new Date(), -24);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getTimestamp("expiry")).thenReturn(new java.sql.Timestamp(pastExpiryDate.getTime()));

        User result = authDao.validateToken("expiredToken");

        assertNull(result);
    }
}
