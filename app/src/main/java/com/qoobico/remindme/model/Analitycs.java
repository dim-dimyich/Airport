package com.qoobico.remindme.model;

import java.io.Serializable;

/**
 * Created by Winner on 31.03.2016.
 */
public class Analitycs implements Serializable {

    String flightHourId, air_model;
    float flight_time;


    public Analitycs() {
    }

    public Analitycs(String flightHourId, String air_model, float flight_time) {
        this.flightHourId = flightHourId;
        this.air_model = air_model;
        this.flight_time = flight_time;

    }

    public String getFlightHourId() {
        return flightHourId;
    }

    public void setFlightHourId(String flightHourId) {
        this.flightHourId = flightHourId;
    }

    public String getAirModel() {
        return air_model;
    }

    public void setAirModel(String air_model) {
        this.air_model = air_model;
    }


    public float getFlightTime() {
        return flight_time;
    }

    public void setFlightTime(float flight_time) {
        this.flight_time = flight_time;
    }


}