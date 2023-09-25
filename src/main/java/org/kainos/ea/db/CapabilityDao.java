package org.kainos.ea.db;

import io.swagger.models.auth.In;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CapabilityDao {
    private DatabaseConnector databaseConnector;

    public CapabilityDao(DatabaseConnector databaseConnector) {
        Objects.requireNonNull(databaseConnector);

        this.databaseConnector = databaseConnector;
    }

    public List<JobCapabilityResponse> getCapabilities() throws SQLException {
        Connection c = databaseConnector.getConnection();

        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery("SELECT capabilityID, capabilityName FROM JobCapability;");

        List<JobCapabilityResponse> capability = new ArrayList<>();

        while (rs.next()) {
            capability.add(new JobCapabilityResponse(
                    rs.getInt("capabilityID"),
                    rs.getString("capabilityName")))
        ;}

        return capability;
    }

    public int deleteCapability(int capabilityID) throws SQLException{
        Connection c = databaseConnector.getConnection();
        String query = "DELETE FROM JobCapability WHERE capabilityID = ?";
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, capabilityID);

        return ps.executeUpdate();
    }

    public ArrayList<Integer> getCapabilityReferences(int capabilityID) throws  SQLException{
        Connection c = databaseConnector.getConnection();
        String query = ("SELECT * FROM JobRoles\n" +
                "WHERE capabilityID = ?;\n");
        ArrayList<Integer> capabilityReferences = new ArrayList<>();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, capabilityID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            capabilityReferences.add(rs.getInt("jobRoleID"));
            ;}
        return capabilityReferences;
    }
}
