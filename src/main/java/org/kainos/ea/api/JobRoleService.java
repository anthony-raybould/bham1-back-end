package org.kainos.ea.api;

import org.eclipse.jetty.server.Authentication;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedJobRolesOperationException;
import org.kainos.ea.client.FailedToUpdateJobRoleException;
import org.kainos.ea.db.JobRoleDao;

import java.security.cert.CertPathBuilder;
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

    public int updateJobRole(Short id, UpdateJobRoleRequest jobRoleRequest) throws FailedJobRolesOperationException, FailedToUpdateJobRoleException {
        try {
            // validator
            return jobRoleDao.updateJobRole(id, jobRoleRequest);
        } catch (SQLException e) {
            throw new FailedJobRolesOperationException("Failed to update job role", e);
        }
    }
}
