package org.kainos.ea.db;

import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedToUpdateJobRoleException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobRoleDao {

    private DatabaseConnector databaseConnector;

    public JobRoleDao(DatabaseConnector databaseConnector) {
        Objects.requireNonNull(databaseConnector);

        this.databaseConnector = databaseConnector;
    }

    public List<JobRoleResponse> getJobRoles() throws SQLException {
        Connection c = databaseConnector.getConnection();

        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery("SELECT jobRoleID, jobRoleName, jobSpecSummary, JobBands.bandID, bandName, JobCapability.capabilityID, capabilityName, responsibilities, sharePoint FROM JobRoles" +
                " INNER JOIN JobBands ON JobRoles.bandID = JobBands.bandID" +
                " INNER JOIN JobCapability ON JobRoles.capabilityID = JobCapability.capabilityID");

        List<JobRoleResponse> jobRoles = new ArrayList<>();

        while (rs.next()) {
            jobRoles.add(new JobRoleResponse(
                    rs.getInt("jobRoleID"),
                    rs.getString("jobRoleName"),
                    rs.getString("jobSpecSummary"),
                    new JobBandResponse(
                            rs.getInt("JobBands.bandID"),
                            rs.getString("bandName")
                    ),
                    new JobCapabilityResponse(
                            rs.getInt("JobCapability.capabilityID"),
                            rs.getString("capabilityName")
                    ),
                    rs.getString("responsibilities"),
                    rs.getString("sharePoint")));
        }

        return jobRoles;
    }

    public int updateJobRole(int id, UpdateJobRoleRequest jobRoleRequest) throws SQLException, FailedToUpdateJobRoleException {
        Connection c = databaseConnector.getConnection();
        String updateQuery = "UPDATE JobRoles" +
                "SET jobRoleName = ?," +
                "jobSpecSummary = ?," +
                "bandID = ?," +
                "capabilityID = ?," +
                "responsibilities = ?," +
                "sharePoint = ?," +
                "WHERE jobRoleID = ?;";

        PreparedStatement preparedStatement = c.prepareStatement(updateQuery);
        preparedStatement.setString(1, jobRoleRequest.getJobRoleName());
        preparedStatement.setString(2, jobRoleRequest.getJobSpecSummary());
        preparedStatement.setInt(3, jobRoleRequest.getBand().getBandID());
        preparedStatement.setInt(4, jobRoleRequest.getCapability().getCapabilityID());
        preparedStatement.setString(5, jobRoleRequest.getResponsibilities());
        preparedStatement.setString(6, jobRoleRequest.getSharePoint());
        preparedStatement.setInt(7, id);

        int rowsUpdated = preparedStatement.executeUpdate();
        if(rowsUpdated > 0)
        {
            return id;
        }
        else {
            throw new FailedToUpdateJobRoleException();
        }
    }
}
