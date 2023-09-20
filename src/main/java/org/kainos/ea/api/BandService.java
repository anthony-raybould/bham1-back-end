package org.kainos.ea.api;

import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cllient.FailedToGetBandsException;
import org.kainos.ea.db.BandDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class BandService {
    private final BandDao bandDao;
    public BandService(BandDao bandDao) {
        Objects.requireNonNull(bandDao);
        this.bandDao = bandDao;
    }

    public List<JobBandResponse> getBands() throws SQLException, FailedToGetBandsException {
        try {
            return bandDao.getBands();
        } catch (SQLException e) {
            throw new FailedToGetBandsException();
        }
    }
}
