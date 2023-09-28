package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCapabilityRequest {

    private String capabilityName;

    @JsonCreator
    public CreateCapabilityRequest(@JsonProperty("capabilityName") String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }
}
