package org.kainos.ea.db;

import org.kainos.ea.cli.JobRole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobRoleDao {

    private DatabaseConnector databaseConnector;

    public JobRoleDao(DatabaseConnector databaseConnector) {
        Objects.requireNonNull(databaseConnector);

        this.databaseConnector = databaseConnector;
    }

    public List<JobRole> getJobRoles() throws SQLException {
        Connection c = databaseConnector.getConnection();

        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery("SELECT jobRoleID, name FROM JobRole");

        List<JobRole> jobRoles = new ArrayList<>();

        while (rs.next()) {
            jobRoles.add(new JobRole(rs.getInt("jobRoleID"), rs.getString("name")));
        }

        return jobRoles;
    }

}
