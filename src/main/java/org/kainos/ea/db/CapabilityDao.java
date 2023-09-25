package org.kainos.ea.db;

import org.kainos.ea.cli.CreateCapabilityRequest;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToCreateCapabilityException;

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

    public int createCapability(CreateCapabilityRequest capabilityRequest) throws FailedToCreateCapabilityException {

        try {
            Connection c = databaseConnector.getConnection();

            String insertString = "INSERT INTO JobCapability (capabilityName) VALUES (?)";
            PreparedStatement st = c.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, capabilityRequest.getCapabilityName());

            st.execute();

            ResultSet results = st.getGeneratedKeys();
            if (results.next()) {
                return results.getInt(1);
            }

            throw new FailedToCreateCapabilityException();

        } catch (SQLException e) {
            throw new FailedToCreateCapabilityException();
        }
    }
}
