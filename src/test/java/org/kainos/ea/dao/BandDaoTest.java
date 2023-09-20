package org.kainos.ea.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.db.BandDao;
import org.kainos.ea.db.DatabaseConnector;
import org.kainos.ea.db.JobRoleDao;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class BandDaoTest {
    DatabaseConnector databaseConnector = mock(DatabaseConnector.class);

    BandDao bandDao;

    @BeforeEach
    public void setup() {
        bandDao = new BandDao(databaseConnector);
    }

    @Test
    public void constructor_shouldThrowNullPointerException_whenDatabaseConnectorIsNull() {
        assertThrows(NullPointerException.class, () -> new BandDao(null));
    }

    @Test
    public void getBand_whenBandsExist_shouldReturn() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        Mockito.when(databaseConnector.getConnection()).thenReturn(connectionMock);

        Statement statementMock = mock(Statement.class);
        ResultSet resultSetMock = mock(ResultSet.class);

        Mockito.when(connectionMock.createStatement()).thenReturn(statementMock);
        Mockito.when(statementMock.executeQuery(any(String.class))).thenReturn(resultSetMock);
        Mockito.when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSetMock.getInt("bandID")).thenReturn(12);
        Mockito.when(resultSetMock.getString("bandName")).thenReturn("summary");

        List<JobBandResponse> response =  bandDao.getBands();

        assertEquals(response.get(0).getBandID(), 12);
        assertEquals(response.get(0).getBandName(), "summary");
    }
}
