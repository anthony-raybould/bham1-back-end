package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.kainos.ea.validator.UpdateJobRoleValidator;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class JobRoleDaoTest {
    DatabaseConnector databaseConnector = mock(DatabaseConnector.class);

    JobRoleDao jobRoleDao = Mockito.mock(JobRoleDao.class);
    JobRoleService jobRoleService;
    UpdateJobRoleValidator updateJobRoleValidator = Mockito.mock(UpdateJobRoleValidator.class);

    @BeforeEach
    public void setup() {
        jobRoleDao = new JobRoleDao(databaseConnector);
        jobRoleService = new JobRoleService(jobRoleDao, updateJobRoleValidator);
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenDatabaseConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new JobRoleDao(null));
    }

    @Test
    public void getJobRoles_shouldReturnJobRoles_whenThereAreJobRoles() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

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
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        List<JobRoleResponse> jobRoles = jobRoleDao.getJobRoles();

        assertEquals(jobRoles.size(), 0);
    }

    @Test
    public void updateJobRole_shouldReturnJobRoleID_whenValidUpdate() throws SQLException, FailedToUpdateJobRoleException {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "sharepointLink");

        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        int id = jobRoleDao.updateJobRole(1, jobRoleRequest);
        assertEquals(1, id, "Expected 1 row to be updated.");
    }
    @Test
    public void updateJobRole_shouldThrowFailedToUpdateJobRoleException_whenCantUpdate() throws SQLException, FailedToUpdateJobRoleException {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "sharepointLink");

        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(-1);
        assertThrows(FailedToUpdateJobRoleException.class,
                () -> jobRoleDao.updateJobRole(1,jobRoleRequest));
    }
    @Test
    public void getJobRole_shouldReturnJobRole_whenThereIsAJobRole() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = Mockito.mock(PreparedStatement.class);
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        Mockito.when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(statementMock);

        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true);
        Mockito.when(resultSetMock.getInt("jobRoleID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("jobRoleName")).thenReturn("Test");
        Mockito.when(resultSetMock.getString("jobSpecSummary")).thenReturn("Test summary");
        Mockito.when(resultSetMock.getInt("JobBands.bandID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("bandName")).thenReturn("Test band");
        Mockito.when(resultSetMock.getInt("JobCapability.capabilityID")).thenReturn(1);
        Mockito.when(resultSetMock.getString("capabilityName")).thenReturn("Test capability");
        Mockito.when(resultSetMock.getString("responsibilities")).thenReturn("Test responsibilities");
        Mockito.when(resultSetMock.getString("sharePoint")).thenReturn("Test sharepoint");

        JobRoleResponse jobRole = jobRoleDao.getJobRoleById(1);

        assertEquals(jobRole.getJobRoleID(), 1);
        assertEquals(jobRole.getJobRoleName(), "Test");
        assertEquals(jobRole.getJobSpecSummary(), "Test summary");
        assertEquals(jobRole.getBand().getBandID(), 1);
        assertEquals(jobRole.getBand().getBandName(), "Test band");
        assertEquals(jobRole.getCapability().getCapabilityID(), 1);
        assertEquals(jobRole.getCapability().getCapabilityName(), "Test capability");
        assertEquals(jobRole.getResponsibilities(), "Test responsibilities");
        assertEquals(jobRole.getSharePoint(), "Test sharepoint");
    }

    @Test
    public void getJobRole_shouldReturnNull_whenThereIsNoJobRole() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = Mockito.mock(PreparedStatement.class);
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery()).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(false);

        JobRoleResponse jobRole = jobRoleDao.getJobRoleById(0);

        assertNull(jobRole);
    }

    @Test
    public void deleteJobRole_shouldReturn1IfJobExists_whenDaoDeletesTheJob() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = Mockito.mock(PreparedStatement.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeUpdate()).thenReturn(1);

        int actualResult = jobRoleDao.deleteJobRole(1);

        assertEquals(1, actualResult);
    }

    @Test
    public void deleteJobRole_shouldReturn0IfDoesNotJobExists_whenDBDeletesNothing() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = Mockito.mock(PreparedStatement.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeUpdate()).thenReturn(0);

        int actualResult = jobRoleDao.deleteJobRole(-1);

        assertEquals(0, actualResult);
    }

    @Test
    public void deleteJobRole_shouldThrowSQLLException_whenDBThrowsSQLException() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        PreparedStatement statementMock = Mockito.mock(PreparedStatement.class);

        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(statementMock);
        Mockito.when(statementMock.executeUpdate()).thenThrow(SQLException.class);

        assertThrows(SQLException.class, () -> jobRoleDao.deleteJobRole(-1));
    }

}
