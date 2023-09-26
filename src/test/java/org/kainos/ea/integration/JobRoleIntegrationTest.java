package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.cli.CreateJobRoleRequest;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    void updateJobRoles_shouldReturn200() {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName",
                "jobSpecSummary",
                1,
                1,
                "jobResponsibility",
                "https://www.something.com/");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(jobRoleRequest, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    @Test
    void updateJobRoles_whenValidationExceptionThrow_Return400() {
        UpdateJobRoleRequest jobRoleRequest = new UpdateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "invalidURL");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(jobRoleRequest , MediaType.APPLICATION_JSON));
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void createJobRoles_shouldReturn200() {
        CreateJobRoleRequest jobRoleRequest = new CreateJobRoleRequest("jobRoleName",
                "jobSpecSummary",
                1,
                1,
                "jobResponsibility",
                "https://www.something.com/");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(jobRoleRequest, MediaType.APPLICATION_JSON));

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void createJobRoles_shouldReturnIdOfJobRole() {
        CreateJobRoleRequest jobRoleRequest = new CreateJobRoleRequest("jobRoleName",
                "jobSpecSummary",
                1,
                1,
                "jobResponsibility",
                "https://www.something.com/");
        int id = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(jobRoleRequest, MediaType.APPLICATION_JSON))
                .readEntity(Integer.class);

        Assertions.assertNotNull(id);
    }

    @Test
    void createJobRoles_whenInvalidJobRoleExceptionThrown_shouldReturn400() {
        CreateJobRoleRequest jobRoleRequest = new CreateJobRoleRequest("jobRoleName", "jobSpecSummary",
                1, 1,
                "jobResponsibility", "invalidURL");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/job-roles")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(jobRoleRequest , MediaType.APPLICATION_JSON));
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
