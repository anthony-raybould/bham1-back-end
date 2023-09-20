package org.kainos.ea.api;

import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.db.CapabilityDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CapabilityService {
    public CapabilityService(CapabilityDao capabilityDao) {
        Objects.requireNonNull(capabilityDao);
        this.capabilityDao = capabilityDao;
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
}
