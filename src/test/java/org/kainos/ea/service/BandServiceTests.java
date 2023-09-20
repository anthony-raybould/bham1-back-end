package org.kainos.ea.service;

import org.junit.jupiter.api.Test;
import org.kainos.ea.api.BandService;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cllient.FailedToGetBandsException;
import org.kainos.ea.db.BandDao;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BandServiceTests {
    BandDao bandDao = Mockito.mock(BandDao.class);
    BandService bandService;

    @Test
    public void constructor_whenNullBandDao_shouldThrowNullPointerException(){
        assertThrows(NullPointerException.class, () -> new BandService(null));
    }

    @Test
    public void getBands_shouldReturnJobBands_whenSuccess() throws SQLException, FailedToGetBandsException {
        List<JobBandResponse> response = new ArrayList<>();
        response.add(new JobBandResponse(1, "jobBand"));

        Mockito.when(bandDao.getBands()).thenReturn(response);
        bandService = new BandService(bandDao);
        assertEquals(response, bandService.getBands());
    }

    @Test
    public void getBands_shouldThrowFailedToGetBandsException_whenSQLException() throws SQLException {
        Mockito.when(bandDao.getBands()).thenThrow(SQLException.class);
        bandService = new BandService(bandDao);
        assertThrows(FailedToGetBandsException.class, () -> bandService.getBands());
    }
}
