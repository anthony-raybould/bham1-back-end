package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateJobRoleRequest {
    private String jobRoleName;
    private String jobSpecSummary;
    private int band;
    private int capability;
    private String responsibilities;
    private String sharePoint;

    @JsonCreator
    public CreateJobRoleRequest(@JsonProperty("jobRoleName") String jobRoleName,
                                @JsonProperty("jobSpecSummary") String jobSpecSummary,
                                @JsonProperty("band") int band,
                                @JsonProperty("capability") int capability,
                                @JsonProperty("responsibilities") String responsibilities,
                                @JsonProperty("sharePoint") String sharePoint) {
        this.jobRoleName = jobRoleName;
        this.jobSpecSummary = jobSpecSummary;
        this.band = band;
        this.capability = capability;
        this.responsibilities = responsibilities;
        this.sharePoint = sharePoint;
    }

    public String getJobRoleName() {
        return jobRoleName;
    }

    public void setJobRoleName(String jobRoleName) {
        this.jobRoleName = jobRoleName;
    }

    public String getJobSpecSummary() {
        return jobSpecSummary;
    }

    public void setJobSpecSummary(String jobSpecSummary) {
        this.jobSpecSummary = jobSpecSummary;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public int getCapability() {
        return capability;
    }

    public void setCapability(int capability) {
        this.capability = capability;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getSharePoint() {
        return sharePoint;
    }

    public void setSharePoint(String sharePoint) {
        this.sharePoint = sharePoint;
    }
}
