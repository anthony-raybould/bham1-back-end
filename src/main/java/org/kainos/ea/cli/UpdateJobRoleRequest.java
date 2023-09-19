package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UpdateJobRoleRequest {
    private String jobRoleName;
    private String jobSpecSummary;
    private JobBandResponse band;
    private JobCapabilityResponse capability;
    private String responsibilities;
    private String sharePoint;

    @JsonCreator
    public UpdateJobRoleRequest(String jobRoleName, String jobSpecSummary, JobBandResponse band, JobCapabilityResponse capability, String responsibilities, String sharePoint) {
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
