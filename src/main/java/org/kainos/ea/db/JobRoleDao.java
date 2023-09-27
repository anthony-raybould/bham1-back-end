package org.kainos.ea.db;

import org.kainos.ea.cli.*;
import org.kainos.ea.client.FailedToCreateJobRoleRequestException;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedToUpdateJobRoleException;

import javax.ws.rs.sse.Sse;
import java.sql.*;
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
    public boolean doesJobRoleExist(int id) throws SQLException {
        Connection c = databaseConnector.getConnection();
        String updateQuery = "SELECT COUNT(*) AS roleExists\n" +
                "FROM JobRoles\n" +
                "WHERE jobRoleID = ?";

        PreparedStatement preparedStatement = c.prepareStatement(updateQuery);
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next())
        {
            if(rs.getInt("roleExists") == 1)
            {
                return true;
            }
        }

        return false;
    }

    public int updateJobRole(int id, UpdateJobRoleRequest jobRoleRequest) throws SQLException, FailedToUpdateJobRoleException {
        Connection c = databaseConnector.getConnection();
        String updateQuery = "UPDATE JobRoles " +
                "SET jobRoleName = ? , " +
                "jobSpecSummary = ? , " +
                "bandID = ? , " +
                "capabilityID = ? , " +
                "responsibilities = ? , " +
                "sharePoint = ? " +
                "WHERE jobRoleID = ?;";

        PreparedStatement preparedStatement = c.prepareStatement(updateQuery);
        preparedStatement.setString(1, jobRoleRequest.getJobRoleName());
        preparedStatement.setString(2, jobRoleRequest.getJobSpecSummary());
        preparedStatement.setInt(3, jobRoleRequest.getBand());
        preparedStatement.setInt(4, jobRoleRequest.getCapabilityID());
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

    public int createJobRole(CreateJobRoleRequest jobRoleRequest) throws SQLException, FailedToCreateJobRoleRequestException {
        Connection c = databaseConnector.getConnection();

        try {
            String insertJobRoleQuery = "INSERT INTO JobRoles (jobRoleName, jobSpecSummary, bandID, capabilityID, responsibilities, sharePoint) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement insertJobRole = c.prepareStatement(insertJobRoleQuery, Statement.RETURN_GENERATED_KEYS);
            insertJobRole.setString(1, jobRoleRequest.getJobRoleName());
            insertJobRole.setString(2, jobRoleRequest.getJobSpecSummary());
            insertJobRole.setInt(3, jobRoleRequest.getBand());
            insertJobRole.setInt(4, jobRoleRequest.getCapability());
            insertJobRole.setString(5, jobRoleRequest.getResponsibilities());
            insertJobRole.setString(6, jobRoleRequest.getSharePoint());

            insertJobRole.executeUpdate();

            ResultSet jobRoleResult = insertJobRole.getGeneratedKeys();

            int insertedId;
            if (jobRoleResult.next()) {
                insertedId = jobRoleResult.getInt(1);
                return insertedId;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw e;
        }
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
