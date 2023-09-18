package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobRole;
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
        Mockito.when(resultSetMock.getString("name")).thenReturn("Test").thenReturn("Another test");

        List<JobRole> jobRoles = jobRoleDao.getJobRoles();

        assertEquals(jobRoles.size(), 2);
        assertEquals(jobRoles.get(0).getJobRoleID(), 1);
        assertEquals(jobRoles.get(0).getName(), "Test");
        assertEquals(jobRoles.get(1).getJobRoleID(), 2);
        assertEquals(jobRoles.get(1).getName(), "Another test");
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

        List<JobRole> jobRoles = jobRoleDao.getJobRoles();

        assertEquals(jobRoles.size(), 0);
    }

}
