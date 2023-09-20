package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateJobRoleRequest {
    @JsonCreator
    public UpdateJobRoleRequest(String jobRoleName, String jobSpecSummary, JobBandResponse band, JobCapabilityResponse capability, String responsibilities, String sharePoint) {
        this.jobRoleName = jobRoleName;
        this.jobSpecSummary = jobSpecSummary;
        this.band = band;
        this.capability = capability;
        this.responsibilities = responsibilities;
        this.sharePoint = sharePoint;
    }
    @JsonProperty("jobRoleName")
    private String jobRoleName;
    @JsonProperty("jobSpecSummary")
    private String jobSpecSummary;
    @JsonProperty("band")
    private JobBandResponse band;
    @JsonProperty("capability")
    private JobCapabilityResponse capability;
    @JsonProperty("responsibilities")
    private String responsibilities;
    @JsonProperty("sharePoint")
    private String sharePoint;

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

    public JobBandResponse getBand() {
        return band;
    }

    public void setBand(JobBandResponse band) {
        this.band = band;
    }

    public JobCapabilityResponse getCapability() {
        return capability;
    }

    public void setCapability(JobCapabilityResponse capability) {
        this.capability = capability;
    }
}
