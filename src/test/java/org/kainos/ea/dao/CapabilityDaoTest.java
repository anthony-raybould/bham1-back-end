package org.kainos.ea.dao;

import io.swagger.models.auth.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.db.DatabaseConnector;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
        when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("capabilityID")).thenReturn(12);
        when(resultSetMock.getString("capabilityName")).thenReturn("summary");

        List<JobCapabilityResponse> response =  capabilityDao.getCapabilities();

        assertEquals(response.get(0).getCapabilityID(), 12);
        assertEquals(response.get(0).getCapabilityName(), "summary");
    }

    @Test
    public void getCapabilityReference_shouldReturnCapabilityReferences_whenExist() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = mock(PreparedStatement.class);
        when(connectionMock.prepareStatement(any(String.class)))
                .thenReturn(statementMock);
        ResultSet resultSetMock = mock(ResultSet.class);
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt(any(String.class))).thenReturn(1);

        ArrayList<Integer> response =  capabilityDao.getCapabilityReferences(1);
        assert(!response.isEmpty());
    }

    @Test
    public void deleteCapability_whenSuccess_returnDeletedID() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        int result =  capabilityDao.deleteCapability(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
        assertEquals(1, result);
    }
}
