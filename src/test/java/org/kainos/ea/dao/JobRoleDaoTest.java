package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class JobRoleDaoTest {

    DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);

    JobRoleDao jobRoleDao;

    @BeforeEach
    public void setup() {
        jobRoleDao = new JobRoleDao(databaseConnector);
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenDatabaseConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new JobRoleDao(null));
    }

    @Test
    public void getJobRoles_shouldReturnJobRoles_whenThereAreJobRoles() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = Mockito.mock(Statement.class);
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getInt("jobRoleID")).thenReturn(1).thenReturn(2);
        Mockito.when(resultSetMock.getString("jobRoleName")).thenReturn("Test").thenReturn("Another test");
        Mockito.when(resultSetMock.getString("jobSpecSummary")).thenReturn("Test summary").thenReturn("Another test summary");
        Mockito.when(resultSetMock.getInt("JobBands.bandID")).thenReturn(1).thenReturn(2);
        Mockito.when(resultSetMock.getString("bandName")).thenReturn("Test band").thenReturn("Another test band");
        Mockito.when(resultSetMock.getInt("JobCapability.capabilityID")).thenReturn(1).thenReturn(2);
        Mockito.when(resultSetMock.getString("capabilityName")).thenReturn("Test capability").thenReturn("Another test capability");
        Mockito.when(resultSetMock.getString("responsibilities")).thenReturn("Test responsibilities").thenReturn("Another test responsibilities");
        Mockito.when(resultSetMock.getString("sharePoint")).thenReturn("Test sharepoint").thenReturn("Another test sharepoint");

        List<JobRoleResponse> jobRoles = jobRoleDao.getJobRoles();

        assertEquals(jobRoles.size(), 2);
        assertEquals(jobRoles.get(0).getJobRoleID(), 1);
        assertEquals(jobRoles.get(0).getJobRoleName(), "Test");
        assertEquals(jobRoles.get(0).getJobSpecSummary(), "Test summary");
        assertEquals(jobRoles.get(0).getBand().getBandID(), 1);
        assertEquals(jobRoles.get(0).getBand().getBandName(), "Test band");
        assertEquals(jobRoles.get(0).getCapability().getCapabilityID(), 1);
        assertEquals(jobRoles.get(0).getCapability().getCapabilityName(), "Test capability");
        assertEquals(jobRoles.get(0).getResponsibilities(), "Test responsibilities");
        assertEquals(jobRoles.get(0).getSharePoint(), "Test sharepoint");
        assertEquals(jobRoles.get(1).getJobRoleID(), 2);
        assertEquals(jobRoles.get(1).getJobRoleName(), "Another test");
        assertEquals(jobRoles.get(1).getJobSpecSummary(), "Another test summary");
        assertEquals(jobRoles.get(1).getBand().getBandID(), 2);
        assertEquals(jobRoles.get(1).getBand().getBandName(), "Another test band");
        assertEquals(jobRoles.get(1).getCapability().getCapabilityID(), 2);
        assertEquals(jobRoles.get(1).getCapability().getCapabilityName(), "Another test capability");
        assertEquals(jobRoles.get(1).getResponsibilities(), "Another test responsibilities");
        assertEquals(jobRoles.get(1).getSharePoint(), "Another test sharepoint");
    }

    @Test
    public void getJobRoles_shouldReturnEmptyJobRoles_whenThereAreNoJobRoles() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = Mockito.mock(Statement.class);
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        List<JobRoleResponse> jobRoles = jobRoleDao.getJobRoles();

        assertEquals(jobRoles.size(), 0);
    }

}
