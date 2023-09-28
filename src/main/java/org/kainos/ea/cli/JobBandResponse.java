package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobBandResponse {


    private int bandID;
    private String bandName;

    @JsonCreator
    public JobBandResponse(@JsonProperty("bandID") int bandID, @JsonProperty("bandName") String bandName) {
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
