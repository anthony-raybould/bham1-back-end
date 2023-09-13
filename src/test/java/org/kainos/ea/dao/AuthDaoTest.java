package org.kainos.ea.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.api.AuthService;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.db.DatabaseConnector;
import org.mockito.Mock;
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
    Login userLogin = new Login(
            "email@email.com",
            "thisIsABadPassword"
    );
    @Test
    public void ctor_shouldThrowNullPointerException_whenNullServiceInCTOR() throws  NullPointerException{
        DatabaseConnector nullDatabaseConnector = null;

        assertThrows(NullPointerException.class,
                () -> new AuthDao(nullDatabaseConnector));
    }

    @Test
    public void validLogin_shouldReturnFalse_whenGetConnectionThrowSqlException() throws SQLException {
        Mockito.when(databaseConnector.getConnection()).thenThrow(SQLException.class);
        assertFalse(
                () -> authDao.validLogin(userLogin)
        );
    }

    @Test
    public void validLogin_shouldReturnTrue_whenValidLogin() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true);
        Mockito.when(resultSetMock.getString("password")).thenReturn("thisIsABadPassword");

        boolean result = authDao.validLogin(userLogin);

        assertTrue(result);
    }

    @Test
    public void getUserId_shouldReturnNegative1_whenCantFindId() throws SQLException, FailedToGetUserId {
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
    public void getUserId_shouldReturnUserId_whenValid() throws SQLException, FailedToGetUserId {
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
    public void generateToken_shouldThrowFailedToGetUserIdException_whenGetUserIdReturnNegative1() throws SQLException, FailedToGetUserId {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        assertThrows(FailedToGetUserId.class,
                () -> authDao.generateToken("email@email.com"));
    }
}
