package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.CreateCapabilityRequest;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToCreateCapabilityException;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    public void createCapability_whenDbError_shouldThrowFailedToCreateCapabilityException() throws SQLException {
        Mockito.when(databaseConnector.getConnection()).thenThrow(new SQLException());
        CreateCapabilityRequest request = new CreateCapabilityRequest("Test Capability");
        assertThrows(FailedToCreateCapabilityException.class, () -> capabilityDao.createCapability(request));
    }

    @Test
    public void createCapability_whenSuccessful_shouldReturnId() throws SQLException, FailedToCreateCapabilityException {

        CreateCapabilityRequest request = new CreateCapabilityRequest("Test Capability");
        int expectedCapabilityId = 1;

        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        Mockito.when(connectionMock.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(statementMock);

        ResultSet resultSetMock = mock(ResultSet.class);
        Mockito.when(statementMock.getGeneratedKeys()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true);
        Mockito.when(resultSetMock.getInt(1)).thenReturn(expectedCapabilityId);

        int actualCapabilityId = capabilityDao.createCapability(request);
        assertEquals(expectedCapabilityId, actualCapabilityId);
    }
}
