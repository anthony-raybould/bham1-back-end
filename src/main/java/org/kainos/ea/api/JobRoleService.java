package org.kainos.ea.api;

import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToDeleteJobRoleException;
import org.kainos.ea.client.JobRoleDoesNotExistException;
import org.kainos.ea.db.JobRoleDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class JobRoleService {
    private final JobRoleDao jobRoleDao;

    public JobRoleService(JobRoleDao jobRoleDao) {
        Objects.requireNonNull(jobRoleDao);

        this.jobRoleDao = jobRoleDao;
    }

    public List<JobRoleResponse> getJobRoles() throws FailedJobRolesOperationException {
        try {
            return jobRoleDao.getJobRoles();
        } catch (SQLException e) {
            throw new FailedJobRolesOperationException("Failed to get job roles from database", e);
        }
    }

    public JobRoleResponse getJobRoleById(int id) throws FailedJobRolesOperationException, JobRoleDoesNotExistException {
        try {
            JobRoleResponse jobRole = jobRoleDao.getJobRoleById(id);

            if (jobRole == null) {
                throw new JobRoleDoesNotExistException(id);
            }

            return jobRole;
        } catch (SQLException e) {
            System.err.println(e.getMessage());

            throw new FailedJobRolesOperationException("Failed to get job role from database", e);
        }
    }

    public int deleteJobRole(int id) throws FailedToDeleteJobRoleException, JobRoleDoesNotExistException {
        try {
            JobRoleResponse jobRoleToDelete = jobRoleDao.getJobRoleById(id);

            if (jobRoleToDelete == null) {
                throw new JobRoleDoesNotExistException(id);
            }

            return jobRoleDao.deleteJobRole(id);
        } catch (SQLException e) {
            System.err.println(e.getMessage());

            throw new FailedToDeleteJobRoleException();
        }
    }

}
