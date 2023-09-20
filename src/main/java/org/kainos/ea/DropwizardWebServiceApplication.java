package org.kainos.ea;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.kainos.ea.api.AuthService;
import org.kainos.ea.auth.JWTService;
import org.kainos.ea.auth.TokenService;
import org.kainos.ea.db.AuthDao;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.resources.AuthController;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.db.JobRoleDao;
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
        bootstrap.addBundle(new SwaggerBundle<DropwizardWebServiceConfiguration>(){
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DropwizardWebServiceConfiguration configuration) {
                return configuration.getSwagger();
            }
        });
    }

    @Override
    public void run(final DropwizardWebServiceConfiguration configuration,
                    final Environment environment) {
        AuthDao authDao = new AuthDao(new DatabaseConnector());

        environment.jersey().register(new AuthController(new AuthService(authDao,new TokenService(authDao, new JWTService()))));
        final DatabaseConnector databaseConnector = new DatabaseConnector();
        final JobRoleDao jobRoleDao = new JobRoleDao(databaseConnector);
        final JobRoleService jobRoleService = new JobRoleService(jobRoleDao);

        environment.jersey().register(new JobRoleController(jobRoleService));
    }

}
