package org.kainos.ea.api;

import org.eclipse.jetty.server.Authentication;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.client.UpdateJobRoleIDDoesNotExistException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.client.UpdateJobRoleIDDoesNotExistException;
import org.kainos.ea.client.FailedToDeleteJobRoleException;
import org.kainos.ea.client.JobRoleDoesNotExistException;
import org.kainos.ea.db.JobRoleDao;
import org.kainos.ea.validator.UpdateJobRoleValidator;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.cert.CertPathBuilder;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class JobRoleService {
    private final JobRoleDao jobRoleDao;
    private final UpdateJobRoleValidator updateJobRoleValidator;

    public JobRoleService(JobRoleDao jobRoleDao, UpdateJobRoleValidator updateJobRoleValidator) {
        Objects.requireNonNull(jobRoleDao);
        Objects.requireNonNull(updateJobRoleValidator);

        this.updateJobRoleValidator = updateJobRoleValidator;
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

    public int updateJobRole(Short id, UpdateJobRoleRequest jobRoleRequest) throws UpdateJobRoleIDDoesNotExistException, ValidationException, FailedToUpdateJobRoleException, FailedJobRolesOperationException {
        try {
            if (jobRoleDao.doesJobRoleExist(id)) {
                updateJobRoleValidator.validate(jobRoleRequest);

                return jobRoleDao.updateJobRole(id, jobRoleRequest);
            }
            throw new UpdateJobRoleIDDoesNotExistException();
        } catch (SQLException e) {
            throw new FailedJobRolesOperationException("Failed to update job role", e);
        }
    }
}
