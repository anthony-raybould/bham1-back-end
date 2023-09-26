package org.kainos.ea.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.api.CapabilityService;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToDeleteCapabilityException;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.client.FailedToGetCapabilityReferences;
import org.kainos.ea.db.CapabilityDao;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class CapabilityServiceTests {
    CapabilityDao capabilityDao = Mockito.mock(CapabilityDao.class);
    CapabilityService capabilityService;
    @BeforeEach()
    public void setup() {
        capabilityService = new CapabilityService(capabilityDao);
    }

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

    @Test
    public void getCapabilityReference_shouldReturnID_whenSuccess() throws SQLException, FailedToGetCapabilityReferences {
        ArrayList<Integer> returnValue = new ArrayList<>();
        returnValue.add(1);
        Mockito.when(capabilityDao.getCapabilityReferences(any(int.class))).thenReturn(returnValue);
        assertEquals(capabilityService.getCapabilityReferences(1), returnValue);
    }
    @Test
    public void getCapabilityReference_throwsFailedToGetCapabilityReferences_whenSQLException() throws SQLException{
        Mockito.when(capabilityDao.getCapabilityReferences(any(int.class))).thenThrow(SQLException.class);
        assertThrows(FailedToGetCapabilityReferences.class, () -> capabilityService.getCapabilityReferences(1));
    }

    @Test
    public void deleteCapability_whenDaoReturnNot1_throwFailedToDeleteCapabilityException() throws SQLException {
        Mockito.when(capabilityDao.deleteCapability(any(int.class))).thenReturn(0);
        assertThrows(FailedToDeleteCapabilityException.class,
                () -> capabilityService.deleteCapability(5));
    }

    @Test
    public void deleteCapability_whenDaoReturn1_returnDeleted() throws SQLException, FailedToDeleteCapabilityException {
        Mockito.when(capabilityDao.deleteCapability(any(int.class))).thenReturn(1);
        assertEquals(capabilityService.deleteCapability(1), 1);
    }

    @Test
    public void deleteCapability_whenSQLException_catchAndThrowFailedToDeleteCapability() throws SQLException {
        Mockito.when(capabilityDao.deleteCapability(any(int.class))).thenThrow(SQLException.class);
        assertThrows(FailedToDeleteCapabilityException.class,
                ()-> capabilityService.deleteCapability(1));
    }
}
