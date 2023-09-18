package org.kainos.ea.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.auth.TokenService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGetUserId;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthDaoTest {
    DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
    AuthDao authDao = new AuthDao(databaseConnector);
    TokenService tokenService = new TokenService(authDao);
    String hashedPassword = BCrypt.hashpw("mySecurePassword", BCrypt.gensalt());
    Login userLogin = new Login(
            "email@email.com",
            hashedPassword
    );
    @Test
    public void constructor_shouldThrowNullPointerException_whenNullServiceInConstructor() throws  NullPointerException{
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
    public void generateToken_shouldThrowFailedToGetUserIdException_whenGetUserIdReturnNegative1() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        assertThrows(FailedToGetUserId.class,
                () -> tokenService.generateToken("email@email.com"));
    }
}
