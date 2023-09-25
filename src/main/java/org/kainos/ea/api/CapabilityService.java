package org.kainos.ea.api;

import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToDeleteCapabilityException;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.client.FailedToGetCapabilityReferences;
import org.kainos.ea.db.CapabilityDao;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public ArrayList<Integer> getCapabilityReferences(int capabilityID) throws SQLException, FailedToGetCapabilityReferences {
        try{
            return capabilityDao.getCapabilityReferences(capabilityID);
        }
        catch (SQLException e)
        {
            throw new FailedToGetCapabilityReferences();
        }
    }
    public int deleteCapability(int capabilityID) throws FailedToDeleteCapabilityException, SQLException{
        try{
            int deleted =  capabilityDao.deleteCapability(capabilityID);
            if(deleted != 1)
            {
                throw new FailedToDeleteCapabilityException();
            }
            return deleted;
        }
        catch (SQLException e)
        {
            throw new FailedToDeleteCapabilityException();
        }
    }
}
