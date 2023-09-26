package org.kainos.ea.api;

import org.eclipse.jetty.server.Authentication;
import org.kainos.ea.cli.CreateJobRoleRequest;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToCreateJobRoleRequestException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.client.InvalidJobRoleException;
import org.kainos.ea.db.JobRoleDao;
import org.kainos.ea.validator.CreateJobRoleValidator;
import org.kainos.ea.validator.UpdateJobRoleValidator;

import javax.validation.ValidationException;
import java.security.cert.CertPathBuilder;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class JobRoleService {
    private final JobRoleDao jobRoleDao;
    private final UpdateJobRoleValidator updateJobRoleValidator;
    private final CreateJobRoleValidator createJobRoleValidator;

    public JobRoleService(JobRoleDao jobRoleDao, UpdateJobRoleValidator  updateJobRoleValidator, CreateJobRoleValidator createJobRoleValidator) {
        Objects.requireNonNull(jobRoleDao);
        Objects.requireNonNull(updateJobRoleValidator);

        this.updateJobRoleValidator = updateJobRoleValidator;
        this.jobRoleDao = jobRoleDao;
        this.createJobRoleValidator = createJobRoleValidator;
    }

    public List<JobRoleResponse> getJobRoles() throws FailedJobRolesOperationException {
        try {
            return jobRoleDao.getJobRoles();
        } catch (SQLException e) {
            throw new FailedJobRolesOperationException("Failed to get job roles from database", e);
        }
    }

    public int updateJobRole(Short id, UpdateJobRoleRequest jobRoleRequest) throws FailedJobRolesOperationException, FailedToUpdateJobRoleException {
        try {
            if(updateJobRoleValidator.validate(jobRoleRequest))
            {
                return jobRoleDao.updateJobRole(id, jobRoleRequest);
            }
            throw new ValidationException();
        } catch (SQLException e) {
            throw new FailedJobRolesOperationException("Failed to update job role", e);
        }
    }

    public int createJobRole(CreateJobRoleRequest jobRoleRequest) throws FailedToCreateJobRoleRequestException, InvalidJobRoleException {
        try {
            String validation = createJobRoleValidator.isValidJobRole(jobRoleRequest);

            if (validation != null) {
                throw new InvalidJobRoleException(validation);
            }
            int id = jobRoleDao.createJobRole(jobRoleRequest);

            if (id == -1) {
                throw new FailedToCreateJobRoleRequestException();
            }
            return id;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new FailedToCreateJobRoleRequestException();
        }
    }
}
