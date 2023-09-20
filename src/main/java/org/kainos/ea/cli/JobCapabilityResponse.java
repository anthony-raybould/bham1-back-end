package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobCapabilityResponse {
    @JsonProperty("capabilityID")
    private int capabilityID;
    @JsonProperty("capabilityName")
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
