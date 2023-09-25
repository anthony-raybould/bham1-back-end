package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.db.DatabaseConnector;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

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
    public void getCapabilityReference_shouldReturnCapabilityReferences_whenExist() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement preparedStatementMock = mock(PreparedStatement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(preparedStatementMock);
        Mockito.when(preparedStatementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getInt("jobRoleID")).thenReturn(1);

        List<Integer> response =  capabilityDao.getCapabilityReferences(1);

        assert(response.size() > 1);
    }

    @Test
    public void deleteCapability_whenSuccess_returnDeletedID(){

    }
}
