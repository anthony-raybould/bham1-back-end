package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.cli.RegisterRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@ExtendWith(DropwizardExtensionsSupport.class)
public class RegisterIntegrationTest {

    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );

    public static String generateRandomEmail() {
        String uuid = UUID.randomUUID().toString();
        return uuid + "@blank.com";
    }

    RegisterRequest registerRequest = new RegisterRequest(generateRandomEmail(), "Test123!", 1);
    RegisterRequest duplicateRequest = new RegisterRequest(System.getenv("TEST_EMAIL"), "Password12345!", 1);

    @Test
    void register_shouldReturn201_whenValidRegistration() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/register").request()
                .post(Entity.entity(registerRequest, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void register_shouldReturn400_whenEmptyRegistration() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/register").request()
                .post(Entity.entity(new RegisterRequest(null, null, 0), MediaType.APPLICATION_JSON));
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void register_shouldReturn400_whenInvalidRegistration() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/register").request()
                .post(Entity.entity(new RegisterRequest("a", "a", 0), MediaType.APPLICATION_JSON));
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void register_shouldReturn400_whenInvalidRole() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/register").request()
                .post(Entity.entity(new RegisterRequest("admin@test.com", "test", -1), MediaType.APPLICATION_JSON));
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void register_shouldReturn409_whenDuplicate() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/register").request()
                .post(Entity.entity(duplicateRequest, MediaType.APPLICATION_JSON));
        System.err.println(response);
        Assertions.assertEquals(409, response.getStatus());
    }
}
