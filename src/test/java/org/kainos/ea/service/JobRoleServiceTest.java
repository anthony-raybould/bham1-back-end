package org.kainos.ea.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.client.UpdateJobRoleIDDoesNotExistException;
import org.kainos.ea.db.JobRoleDao;
import org.kainos.ea.validator.UpdateJobRoleValidator;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class JobRoleServiceTest {

    JobRoleDao jobRoleDao = Mockito.mock(JobRoleDao.class);
    UpdateJobRoleValidator updateJobRoleValidator = Mockito.mock(UpdateJobRoleValidator.class);
    JobRoleService jobRoleService;
    UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName", "jobSpecSummary",1,1,
            "jobResponsibility", "sharepointLink");

    @BeforeEach
    public void setup() {
        jobRoleService = new JobRoleService(jobRoleDao,updateJobRoleValidator);
    }

    @Test
    public void getJobRoles_shouldReturnJobRoles_whenDaoReturnsJobRoles() throws SQLException, FailedJobRolesOperationException {
        List<JobRoleResponse> jobRoles = new ArrayList<>();
        jobRoles.add(new JobRoleResponse(
                1,
                "Test",
                "Test summary",
                new JobBandResponse(1, "Test band"),
                new JobCapabilityResponse(1, "Test capability"),
                "Test responsibilities",
                "Test sharepoint")
        );

        Mockito.when(jobRoleDao.getJobRoles()).thenReturn(jobRoles);

        List<JobRoleResponse> returnedJobRoles = jobRoleService.getJobRoles();

        assertEquals(returnedJobRoles.size(), 1);
        assertEquals(returnedJobRoles.get(0).getJobRoleID(), 1);
        assertEquals(returnedJobRoles.get(0).getJobRoleName(), "Test");
        assertEquals(returnedJobRoles.get(0).getJobSpecSummary(), "Test summary");
        assertEquals(returnedJobRoles.get(0).getBand().getBandID(), 1);
        assertEquals(returnedJobRoles.get(0).getBand().getBandName(), "Test band");
        assertEquals(returnedJobRoles.get(0).getCapability().getCapabilityID(), 1);
        assertEquals(returnedJobRoles.get(0).getCapability().getCapabilityName(), "Test capability");
        assertEquals(returnedJobRoles.get(0).getResponsibilities(), "Test responsibilities");
        assertEquals(returnedJobRoles.get(0).getSharePoint(), "Test sharepoint");
    }

    @Test
    public void getJobRoles_shouldThrowFailedJobRolesExceptionWithMessage_whenDaoThrowsSqlException() throws SQLException {
        String expectedMessage = "Failed to get job roles from database";
        Class<SQLException> expectedCauseClass = SQLException.class;

        Mockito.when(jobRoleDao.getJobRoles()).thenThrow(SQLException.class);

        assertThrows(FailedJobRolesOperationException.class, () -> jobRoleService.getJobRoles());

        try {
            jobRoleService.getJobRoles();
        } catch (FailedJobRolesOperationException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedCauseClass, e.getCause().getClass());
        }
    }
    @Test
    public void updateJobRole_shouldThrowFailedJobRolesException_whenDaoThrowsSqlException() throws SQLException, FailedToUpdateJobRoleException {
        Mockito.when(jobRoleDao.updateJobRole(any(int.class), any(UpdateJobRoleRequest.class))).thenThrow(SQLException.class);
        Mockito.when(jobRoleDao.doesJobRoleExist(any(int.class))).thenReturn(true);
        assertThrows(FailedJobRolesOperationException.class,
                () -> jobRoleService.updateJobRole((short) 1,jobRoleRequest));
    }
    @Test
    public void updateJobRole_shouldThrowUpdateJobRoleIDDoesNotExistException_whenUserIdNotInDB() throws SQLException, FailedToUpdateJobRoleException {
        Mockito.when(jobRoleDao.updateJobRole(any(int.class), any(UpdateJobRoleRequest.class))).thenThrow(SQLException.class);
        Mockito.when(jobRoleDao.doesJobRoleExist(any(int.class))).thenReturn(false);
        assertThrows(UpdateJobRoleIDDoesNotExistException.class,
                () -> jobRoleService.updateJobRole((short) 1,jobRoleRequest));
    }
    @Test
    public void updateJobRole_shouldThrowValidationException_whenInvalidJobRoleRequest() throws ValidationException, FailedToUpdateJobRoleException, SQLException {
        Mockito.when(jobRoleDao.doesJobRoleExist(any(int.class))).thenReturn(true);
        assertThrows(ValidationException.class,
                () -> new JobRoleService(jobRoleDao, new UpdateJobRoleValidator()).updateJobRole((short)1, jobRoleRequest));
    }
    @Test
    public void updateJobRole_shouldReturnID_whenSuccess() throws SQLException, FailedToUpdateJobRoleException, FailedJobRolesOperationException, UpdateJobRoleIDDoesNotExistException {
        Mockito.when(jobRoleDao.updateJobRole(any(int.class), any(UpdateJobRoleRequest.class))).thenReturn(1);
        Mockito.when(jobRoleDao.doesJobRoleExist(any(int.class))).thenReturn(true);
        assertEquals(1, jobRoleService.updateJobRole((short) 1, jobRoleRequest));
    }
}
