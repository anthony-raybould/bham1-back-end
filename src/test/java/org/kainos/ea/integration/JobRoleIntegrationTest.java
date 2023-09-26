package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.integration.helpers.AuthenticateUser;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@ExtendWith(DropwizardExtensionsSupport.class)
public class JobRoleIntegrationTest {

    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );
    private final AuthenticateUser authenticateUser = new AuthenticateUser();

    @Test
    void jobRoles_shouldReturn200() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles").request()
                .header("Authorization", "Bearer " + authenticateUser.loginAdmin()).get();
        Assertions.assertEquals(200, response.getStatus());
    }
    @Test
    void updateJobRoles_shouldReturn200() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName",
                "jobSpecSummary",
                1,
                2,
                "jobResponsibility",
                "https://www.something.com/");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles/1")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authenticateUser.loginAdmin())
                .put(Entity.entity(jobRoleRequest, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    @Test
    void updateJobRoles_whenIdSuppliedNotExist_Return400() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "invalidURL");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles/999")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authenticateUser.loginAdmin())
                .put(Entity.entity(jobRoleRequest , MediaType.APPLICATION_JSON));
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    @Test
    void updateJobRoles_whenValidationExceptionThrow_Return400() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "invalidURL");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles/1")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authenticateUser.loginAdmin())
                .put(Entity.entity(jobRoleRequest , MediaType.APPLICATION_JSON));
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
