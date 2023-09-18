package org.kainos.ea.cli;

public class JobRole {

    private int jobRoleID;
    private String name;

    public JobRole(int jobRoleID, String name) {
        this.jobRoleID = jobRoleID;
        this.name = name;
    }

    public int getJobRoleID() {
        return jobRoleID;
    }

    public void setJobRoleID(int jobRoleID) {
        this.jobRoleID = jobRoleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
