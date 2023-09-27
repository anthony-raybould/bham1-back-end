package org.kainos.ea.api;

import org.kainos.ea.cli.CreateCapabilityRequest;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToCreateCapabilityException;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.validator.CapabilityValidator;

import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CapabilityService {

    CapabilityValidator capabilityValidator;
    public CapabilityService(CapabilityDao capabilityDao, CapabilityValidator capabilityValidator) {
        Objects.requireNonNull(capabilityDao);
        Objects.requireNonNull(capabilityValidator);
        this.capabilityDao = capabilityDao;
        this.capabilityValidator = capabilityValidator;
    }

    private final CapabilityDao  capabilityDao;
    public List<JobCapabilityResponse> getCapabilities() throws FailedToGetCapabilitiesException, SQLException {
        try{
            return capabilityDao.getCapabilities();
        }
        catch (SQLException e){
            throw new FailedToGetCapabilitiesException();
        }
    }

    public int createCapability(CreateCapabilityRequest capabilityRequest) throws FailedToCreateCapabilityException, ValidationException {

        capabilityValidator.validate(capabilityRequest);
        return capabilityDao.createCapability(capabilityRequest);
    }
}
