package org.kainos.ea;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.kainos.ea.api.BandService;
import org.kainos.ea.api.CapabilityService;
import org.kainos.ea.api.JobRoleService;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.db.BandDao;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.kainos.ea.resources.BandController;
import org.kainos.ea.resources.CapabilityController;
import org.kainos.ea.resources.JobRoleController;
import org.kainos.ea.validator.CapabilityValidator;
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
        // TODO: implement application
        final DatabaseConnector databaseConnector = new DatabaseConnector();
        final JobRoleDao jobRoleDao = new JobRoleDao(databaseConnector);
        final UpdateJobRoleValidator updateJobRoleValidator = new UpdateJobRoleValidator();
        final JobRoleService jobRoleService = new JobRoleService(jobRoleDao, updateJobRoleValidator);
        final CapabilityDao capabilityDao = new CapabilityDao(databaseConnector);
        final BandDao bandDao = new BandDao(databaseConnector);
        final CapabilityValidator capabilityValidator = new CapabilityValidator();
        final CapabilityService capabilityService = new CapabilityService(capabilityDao, capabilityValidator);
        final BandService bandService = new BandService(bandDao);   
        environment.jersey().register(new JobRoleController(jobRoleService));
        environment.jersey().register(new BandController(bandService));
        environment.jersey().register(new CapabilityController(capabilityService));
    }

}
