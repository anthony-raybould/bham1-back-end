package org.kainos.ea.integration;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kainos.ea.DropwizardWebServiceApplication;
import org.kainos.ea.DropwizardWebServiceConfiguration;
import org.kainos.ea.client.FailedToGenerateTokenException;
import org.kainos.ea.client.FailedToGetUserId;
import org.kainos.ea.integration.helpers.AuthenticateUser;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BandIntegrationTest {
    static final DropwizardAppExtension<DropwizardWebServiceConfiguration> APP = new DropwizardAppExtension<>(
            DropwizardWebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );
    private final AuthenticateUser authenticateUser = new AuthenticateUser();

    @Test
    void band_shouldReturn200() throws FailedToGetUserId, SQLException, FailedToGenerateTokenException {
        Response response = APP.client().target(System.getenv("TARGET_DOMAIN") + "/api/band").request()
                .header("Authorization", "Bearer " + authenticateUser.loginAdmin()).get();
        Assertions.assertEquals(200, response.getStatus());
    }
}
