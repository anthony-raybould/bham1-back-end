package org.kainos.ea.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.*;
import org.kainos.ea.client.FailedToCreateJobRoleRequestException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.nio.file.FileAlreadyExistsException;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

public class JobRoleDaoTest {
    DatabaseConnector databaseConnector = mock(DatabaseConnector.class);

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
    public void createJobRole_shouldReturnId_whenValidJobRole() throws SQLException, FailedToCreateJobRoleRequestException {
        CreateJobRoleRequest jobRoleRequest = new CreateJobRoleRequest("testName",
                                                                            "testJobSpec",
                                                                            1,
                                                                            1,
                                                                            "testResponsibilities",
                                                                            "https://kainossoftwareltd.sharepoint.com/:b:/r/people/Job%20Specifications/Engineering/Job%20profile%20-%20Software%20Engineer%20(Trainee).pdf?csf=1&web=1&e=nQzHld"
        );
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        Mockito.when(connectionMock.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);


        ResultSet resultSet = mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt(anyInt())).thenReturn(1);
        int id = jobRoleDao.createJobRole(jobRoleRequest);
        Assertions.assertEquals(1,id);
    }

    @Test
    public void createJobRole_shouldThrowSQLException_whenCantCreate() throws SQLException {
        CreateJobRoleRequest jobRoleRequest = new CreateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "sharepointLink");

        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenThrow(SQLException.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        Mockito.when(connectionMock.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(-1);
        assertThrows(SQLException.class,
                () -> jobRoleDao.createJobRole(jobRoleRequest));
    }
}
