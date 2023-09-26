package org.kainos.ea;

import io.dropwizard.Application;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.checkerframework.checker.units.qual.A;
import org.kainos.ea.api.*;
import org.kainos.ea.auth.*;
import org.kainos.ea.db.*;
import org.kainos.ea.resources.AuthController;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.kainos.ea.cli.User;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.resources.BandController;
import org.kainos.ea.resources.CapabilityController;
import org.kainos.ea.resources.JobRoleController;
import org.kainos.ea.validator.UpdateJobRoleValidator;

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
        final UpdateJobRoleValidator jobRoleValidator = new UpdateJobRoleValidator();
        final JobRoleDao jobRoleDao = new JobRoleDao(databaseConnector);
        final JobRoleService jobRoleService = new JobRoleService(jobRoleDao, jobRoleValidator);
        final CapabilityDao capabilityDao = new CapabilityDao(databaseConnector);
        final BandDao bandDao = new BandDao(databaseConnector);
        final CapabilityService capabilityService = new CapabilityService(capabilityDao);
        final BandService bandService = new BandService(bandDao);
        final AuthDao authDao = new AuthDao(databaseConnector);
        final JWTService jwtService = new JWTService();
        final TokenService tokenService = new TokenService(authDao, jwtService);
        final AuthService authService = new AuthService(authDao, tokenService);
        environment.jersey().register(new AuthController(authService));
        environment.jersey().register(new JobRoleController(jobRoleService));
        environment.jersey().register(new BandController(bandService));
        environment.jersey().register(new CapabilityController(capabilityService));

        environment.jersey().register(new AuthDynamicFeature(
                new TokenAuthFilter.Builder()
                        .setAuthenticator(new TokenAuthenticator(tokenService))
                        .setAuthorizer(new TokenAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

}
