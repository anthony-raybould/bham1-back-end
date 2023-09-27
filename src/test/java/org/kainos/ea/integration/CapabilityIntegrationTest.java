package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.db.DatabaseConnector;

import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CapabilityIntegrationTest {
    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );

    @Test
    void capability_shouldReturn200() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/capabilities").request().get();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deleteCapability_shouldReturn200() throws SQLException {
        int idToDelete = createCapabilityToDelete();
        Response response = APP.client().target(
                        System.getenv("TARGET_DOMAIN")+ "/api/capabilities/" + idToDelete)
                .request().delete();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deleteCapability_shouldReturn409_whenReferenced() throws SQLException {
        int capability = createCapabilityToDelete();
        int capabilityReference = createReferenceToCapability(capability);

        Response response = APP.client().target(
                System.getenv("TARGET_DOMAIN") + "/api/capabilities/" + capability).request().delete();
        Assertions.assertEquals(409, response.getStatus());
        deleteReference(capabilityReference);
        deleteCapability(capability);
    }

    private void deleteReference(int referenceID) throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();
        String deleteReferenceQuery = "DELETE FROM `JobRoles` WHERE jobRoleID = ?;";
        PreparedStatement preparedStatement = connector.getConnection().prepareStatement(deleteReferenceQuery);
        preparedStatement.setInt(1, referenceID);
        preparedStatement.executeUpdate();
    }
    private void deleteCapability(int capabilityID) throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();
        String deleteCapabilityQuery = "DELETE FROM JobCapability WHERE capabilityID = ?;";
        PreparedStatement preparedStatement = connector.getConnection().prepareStatement(deleteCapabilityQuery);
        preparedStatement.setInt(1, capabilityID);
        preparedStatement.executeUpdate();
    }

    private int createCapabilityToDelete() throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();
        String query = "INSERT into JobCapability(capabilityName) VALUES ('testCapability')";

        Statement st = connector.getConnection().createStatement();
        st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys = st.getGeneratedKeys();

        while (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1;
    }

    private int createReferenceToCapability(int capabilityID) throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();
        String query = "INSERT INTO JobRoles(jobRoleName, jobSpecSummary, bandID, capabilityID, responsibilities, sharePoint)  VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = connector.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, "referenceJobRoleName");
        ps.setString(2, "referenceJobSpecSummary");
        ps.setInt(3, 1);
        ps.setInt(4, capabilityID);
        ps.setString(5, "referenceResponsibilities");
        ps.setString(6, "referenceSharePoint");
        ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        while (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1;    }
}
