package org.kainos.ea.db;

import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.JobRoleResponse;

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

   public JobRoleResponse getJobRoleById(int id) throws SQLException {
        Connection c = databaseConnector.getConnection();

        String query = "SELECT jobRoleID, jobRoleName, jobSpecSummary, JobBands.bandID, bandName, JobCapability.capabilityID, capabilityName, responsibilities, sharePoint FROM JobRoles" +
                " INNER JOIN JobBands ON JobRoles.bandID = JobBands.bandID" +
                " INNER JOIN JobCapability ON JobRoles.capabilityID = JobCapability.capabilityID WHERE jobRoleID = ?";

        PreparedStatement st = c.prepareStatement(query);
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return new JobRoleResponse(rs.getInt("jobRoleID"),
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
                    rs.getString("sharePoint"));
        }
        return null;
    }

    public int  deleteJobRole(int id) throws SQLException {
        Connection c = databaseConnector.getConnection();

        String deleteJobRoleQuery = "DELETE FROM JobRoles WHERE jobRoleID = ?";

        PreparedStatement deleteJobRole = c.prepareStatement(deleteJobRoleQuery);

        deleteJobRole.setInt(1, id);

        return deleteJobRole.executeUpdate();

    }


}
