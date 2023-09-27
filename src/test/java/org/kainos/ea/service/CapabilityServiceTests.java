package org.kainos.ea.service;

import org.junit.jupiter.api.Test;
import org.kainos.ea.api.CapabilityService;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.client.FailedToCreateCapabilityException;
import org.kainos.ea.client.FailedToGetCapabilitiesException;
import org.kainos.ea.db.CapabilityDao;
import org.kainos.ea.validator.CapabilityValidator;
import org.mockito.Mockito;

import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CapabilityServiceTests {
    CapabilityDao capabilityDao = Mockito.mock(CapabilityDao.class);
    CapabilityValidator capabilityValidator = Mockito.mock(CapabilityValidator.class);
    CapabilityService capabilityService;

    @Test
    public void constructor_whenNullCapabilityDao_shouldThrowNullPointerException(){
        assertThrows(NullPointerException.class, () -> new CapabilityService(null, capabilityValidator));
    }

    @Test
    public void constructor_whenNullCapabilityValidator_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CapabilityService(capabilityDao, null));
    }

    @Test
    public void getCapabilities_shouldReturnCapabilities_whenSuccess() throws SQLException, FailedToGetCapabilitiesException {
        List<JobCapabilityResponse> response = new ArrayList<>();
        response.add(new JobCapabilityResponse(1, "capability"));

        Mockito.when(capabilityDao.getCapabilities()).thenReturn(response);
        capabilityService = new CapabilityService(capabilityDao, capabilityValidator);
        assertEquals(response, capabilityService.getCapabilities());
    }

    @Test
    public void getCapabilities_shouldThrowFailedToGetCapabilityException_whenSQLException() throws SQLException {
        Mockito.when(capabilityDao.getCapabilities()).thenThrow(SQLException.class);
        capabilityService = new CapabilityService(capabilityDao, capabilityValidator);
        assertThrows(FailedToGetCapabilitiesException.class, () -> capabilityService.getCapabilities());
    }

    @Test
    public void createCapability_shouldThrowFailedToCreateCapabilityException_whenDaoThrowsFailedToCreateCapabilityException() throws FailedToCreateCapabilityException {
        Mockito.when(capabilityDao.createCapability(null)).thenThrow(FailedToCreateCapabilityException.class);

        capabilityService = new CapabilityService(capabilityDao, capabilityValidator);

        assertThrows(FailedToCreateCapabilityException.class, () -> capabilityService.createCapability(null));
    }

    @Test
    public void createCapability_shouldThrowValidationException_whenValidatorThrowsValidationException() throws FailedToCreateCapabilityException {

        Mockito.doThrow(new ValidationException()).when(capabilityValidator).validate(null);
        Mockito.when(capabilityDao.createCapability(null)).thenReturn(1);

        capabilityService = new CapabilityService(capabilityDao, capabilityValidator);

        assertThrows(ValidationException.class, () -> capabilityService.createCapability(null));
    }

    @Test
    public void createCapability_shouldReturnId_whenDaoReturnsId() throws FailedToCreateCapabilityException {

        int expectedId = 1;
        Mockito.when(capabilityDao.createCapability(null)).thenReturn(expectedId);

        capabilityService = new CapabilityService(capabilityDao, capabilityValidator);
        int actualId = capabilityService.createCapability(null);

        assertEquals(expectedId, actualId);
    }
}
