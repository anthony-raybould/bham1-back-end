package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.JobRoleResponse;
import org.kainos.ea.client.FailedToDeleteJobRoleException;
import org.kainos.ea.client.JobRoleDoesNotExistException;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.resources.JobRoleController;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class JobRoleIntegrationTest {



    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );
    @Test
    void jobRoles_shouldReturn200() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles").request().get();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void getJobRoleById_shouldReturn200_whenValidInput() {
        Response response = APP.client().target("http://localhost:8080/api/job-roles/1")
                .request()
                .get();
        Assertions.assertEquals(200,response.getStatus());
        Assertions.assertEquals(1, response.readEntity(JobRoleResponse.class).getJobRoleID());
    }

    @Test
    public void deleteJobRole_shouldReturn200_whenJobDeleted() throws JobRoleDoesNotExistException, FailedToDeleteJobRoleException, SQLException {
        int userId = createTestUser();
        Response response = APP.client().target("http://localhost:8080/api/job-roles/" + userId)
                .request()
                .delete();
        Assertions.assertEquals(200,response.getStatus());
    }

    @Test
    public void deleteJobRole_shouldReturn400_whenInvalidUser() throws JobRoleDoesNotExistException, FailedToDeleteJobRoleException, SQLException {
        Response response = APP.client().target("http://localhost:8080/api/job-roles/777")
                .request()
                .delete();
        Assertions.assertEquals(400,response.getStatus());
    }

    public int createTestUser() throws SQLException {
        DatabaseConnector connector = new DatabaseConnector();
        String query = "INSERT into JobRoles(jobRoleID, jobRoleName,jobSpecSummary,bandID,capabilityID,responsibilities,sharePoint) VALUES (1, 'testName', 'testSummary', 1, 1, 'responsibilities', 'sharePoint')";

        Statement st = connector.getConnection().createStatement();
        int affectedRows = st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys = st.getGeneratedKeys();

        while (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1;
    }

}
