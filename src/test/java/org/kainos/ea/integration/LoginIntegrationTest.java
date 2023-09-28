package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.cli.Login;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


@ExtendWith(DropwizardExtensionsSupport.class)
public class LoginIntegrationTest {

    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );
    Login invalidLoginRequest = new Login("user@user.com", "invalidPassword");

    @Test
    void login_shouldReturn200_whenValidLogin() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        Login validLoginRequest = new Login(System.getenv("TEST_EMAIL"), System.getenv("TEST_PWD"));
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/login").request()
                .post(Entity.entity(validLoginRequest, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void login_shouldReturn401_whenInValidLogin() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/login").request()
                .post(Entity.entity(invalidLoginRequest, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    void login_shouldReturn500_whenInternalServerError() {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/login").request()
                .post(Entity.entity(null, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(500, response.getStatus());
    }
}
