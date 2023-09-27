package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class CapabilityDaoTest {
    DatabaseConnector databaseConnector = mock(DatabaseConnector.class);

    CapabilityDao capabilityDao;

    @BeforeEach
    public void setup() {
        capabilityDao = new CapabilityDao(databaseConnector);
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenDatabaseConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new CapabilityDao(null));
    }
    @Test
    public void getBand_whenDbError_shouldThrowSQLException() throws SQLException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(null);
        assertThrows(SQLException.class, () -> capabilityDao.getCapabilities());
    }
    @Test
    public void getCapabilities_shouldReturnCapabilities_whenExist() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getInt("capabilityID")).thenReturn(12);
        Mockito.when(resultSetMock.getString("capabilityName")).thenReturn("summary");

        List<JobCapabilityResponse> response =  capabilityDao.getCapabilities();

        assertEquals(response.get(0).getCapabilityID(), 12);
        assertEquals(response.get(0).getCapabilityName(), "summary");
    }

    @Test
    public void doesCapabilityExist_shouldThrowSQLException_whenDBFailure() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenThrow(SQLException.class);

        assertThrows(SQLException.class, () -> capabilityDao.doesCapabilityExist(1));
    }

    @Test
    public void doesCapabilityExist_shouldReturnTrue_whenCapabilityExists() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true);

        assertTrue(capabilityDao.doesCapabilityExist(1));
    }

    @Test
    public void doesCapabilityExist_shouldReturnFalse_whenCapabilityDoesNotExist() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        assertFalse(capabilityDao.doesCapabilityExist(1));
    }
}
