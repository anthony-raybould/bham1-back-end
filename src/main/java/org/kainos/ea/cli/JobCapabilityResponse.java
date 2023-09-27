package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobCapabilityResponse {
    private int capabilityID;

    private String capabilityName;

    @JsonCreator
    public JobCapabilityResponse(@JsonProperty("capabilityID") int capabilityID,
                                 @JsonProperty("capabilityName") String capabilityName) {
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
