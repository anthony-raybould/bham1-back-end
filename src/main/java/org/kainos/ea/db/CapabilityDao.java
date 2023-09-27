package org.kainos.ea.db;

import org.kainos.ea.cli.CreateJobRoleRequest;
import org.kainos.ea.cli.JobCapabilityResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        try {


            Connection c = databaseConnector.getConnection();

            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT capabilityID, capabilityName FROM JobCapability;");

            List<JobCapabilityResponse> capability = new ArrayList<>();

            while (rs.next()) {
                capability.add(new JobCapabilityResponse(
                        rs.getInt("capabilityID"),
                        rs.getString("capabilityName")))
                ;
            }

            return capability;
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    public boolean doesCapabilityExist(int capabilityId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery("SELECT * " +
                "FROM JobCapability " +
                "WHERE capabilityID = " + capabilityId);

        return rs.next();
    }
}
