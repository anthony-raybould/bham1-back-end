package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.cli.CreateCapabilityRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    void createCapability_whenValid_shouldReturn200() {

        CreateCapabilityRequest request = new CreateCapabilityRequest("Test Capability Integration");

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/capabilities/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void createCapability_whenInvalid_shouldReturn400() {
        CreateCapabilityRequest request = new CreateCapabilityRequest(null);

        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/capabilities/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
