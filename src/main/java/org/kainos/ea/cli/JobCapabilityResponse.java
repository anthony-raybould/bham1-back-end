package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;

public class JobCapabilityResponse {

    private int capabilityID;
    private String capabilityName;

    @JsonCreator
    public JobCapabilityResponse(int capabilityID, String capabilityName) {
        this.capabilityID = capabilityID;
        this.capabilityName = capabilityName;
    }

    public int getCapabilityID() {
        return capabilityID;
    }

    public void setCapabilityID(int capabilityID) {
        this.capabilityID = capabilityID;
    }

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }
}
