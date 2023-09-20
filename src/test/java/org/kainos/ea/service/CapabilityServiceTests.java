package org.kainos.ea.service;

import org.junit.jupiter.api.Test;
import org.kainos.ea.api.BandService;
import org.kainos.ea.api.CapabilityService;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.cllient.FailedToGetBandsException;
import org.kainos.ea.db.BandDao;
import org.kainos.ea.db.CapabilityDao;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CapabilityServiceTests {
    CapabilityDao capabilityDao = Mockito.mock(CapabilityDao.class);
    CapabilityService capabilityService;

    @Test
    public void constructor_whenNullBCapabilityDao_shouldThrowNullPointerException(){
        assertThrows(NullPointerException.class, () -> new CapabilityService(null));
    }

    @Test
    public void getBands_shouldReturnCapabilities_whenSuccess() throws SQLException, FailedToGetCapabilitiesException {
        List<JobCapabilityResponse> response = new ArrayList<>();
        response.add(new JobCapabilityResponse(1, "capability"));

        Mockito.when(capabilityDao.getCapabilities()).thenReturn(response);
        capabilityService = new CapabilityService(capabilityDao);
        assertEquals(response, capabilityService.getCapabilities());
    }

    @Test
    public void getBands_shouldThrowFailedToGetCapabilityException_whenSQLException() throws SQLException {
        Mockito.when(capabilityDao.getCapabilities()).thenThrow(SQLException.class);
        capabilityService = new CapabilityService(capabilityDao);
        assertThrows(FailedToGetCapabilitiesException.class, () -> capabilityService.getCapabilities());
    }
}
