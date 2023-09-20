package org.kainos.ea;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.resources.AuthController;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.kainos.ea.auth.TokenAuthFilter;
import org.kainos.ea.auth.TokenAuthenticator;
import org.kainos.ea.auth.TokenAuthorizer;
import org.kainos.ea.cli.User;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.db.JobRoleDao;
import org.kainos.ea.resources.BandController;
import org.kainos.ea.resources.CapabilityController;
import org.kainos.ea.resources.JobRoleController;

public class DropwizardWebServiceApplication extends Application<DropwizardWebServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardWebServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardWebService";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardWebServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<DropwizardWebServiceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DropwizardWebServiceConfiguration configuration) {
                return configuration.getSwagger();
            }
        });
    }

    @Override
    public void run(final DropwizardWebServiceConfiguration configuration,
                    final Environment environment) {
        final DatabaseConnector databaseConnector = new DatabaseConnector();

        final JobRoleDao jobRoleDao = new JobRoleDao(databaseConnector);
        final AuthDao authDao = new AuthDao(databaseConnector);

        final JWTService jwtService = new JWTService();
        final TokenService tokenService = new TokenService(authDao, jwtService);
        final AuthService authService = new AuthService(authDao, tokenService);
        final JobRoleService jobRoleService = new JobRoleService(jobRoleDao);

        environment.jersey().register(new JobRoleController(jobRoleService));
        environment.jersey().register(new BandController(bandService));
        environment.jersey().register(new CapabilityController(capabilityService));
    }

}
