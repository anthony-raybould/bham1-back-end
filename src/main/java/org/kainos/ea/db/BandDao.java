package org.kainos.ea.db;


import org.kainos.ea.cli.JobBandResponse;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BandDao {
    private DatabaseConnector databaseConnector;

    public BandDao(DatabaseConnector databaseConnector) {
        Objects.requireNonNull(databaseConnector);

        this.databaseConnector = databaseConnector;
    }

    public List<JobBandResponse> getBands() throws SQLException {
        try{
            Connection c = databaseConnector.getConnection();

            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery("SELECT bandID, bandName FROM JobBands;");

            List<JobBandResponse> band = new ArrayList<>();

            while (rs.next()) {
                band.add(new JobBandResponse(
                        rs.getInt("bandID"),
                        rs.getString("bandName")))
                ;}

            return band;
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    public boolean doesBandExist(int bandId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery("SELECT * " +
                "FROM JobBands " +
                "WHERE bandID = " + bandId);

        return rs.next();
    }
}
