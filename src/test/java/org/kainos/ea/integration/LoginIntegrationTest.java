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

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ExtendWith(DropwizardExtensionsSupport.class)
public class LoginIntegrationTest {

    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );
    Login validLoginRequest = new Login("admin@admin.com", "securePassword");
    Login invalidLoginRequest = new Login("user@user.com", "invalidPassword");

    @Test
    void login_shouldReturn200_whenValidLogin(){
        Response response = APP.client().target("http://localhost:8080/api/login").request()
                .post(Entity.entity(validLoginRequest, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(200, response.getStatus());
    }
    @Test
    void login_shouldReturn401_whenInValidLogin(){
        Response response = APP.client().target("http://localhost:8080/api/login").request()
                .post(Entity.entity(invalidLoginRequest, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(401, response.getStatus());
    }
}
