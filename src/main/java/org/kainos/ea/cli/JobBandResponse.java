package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobBandResponse {
    @JsonProperty("bandID")
    private int bandID;
    @JsonProperty("bandName")
    private String bandName;

    @JsonCreator
    public JobBandResponse(int bandID, String bandName) {
        this.bandID = bandID;
        this.bandName = bandName;
    }

    public int getBandID() {
        return bandID;
    }

    public void setBandID(int bandID) {
        this.bandID = bandID;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }
}
