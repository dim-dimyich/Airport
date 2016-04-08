package com.qoobico.remindme.model;

import java.io.Serializable;

/**
 * Created by Winner on 08.03.2016.
 */
public class FlightCrew implements Serializable {

    String id, crew_name, created;
    User user;
    private int photo;


    public FlightCrew() {
    }

    public FlightCrew(String id, String crew_name, User user) {
        this.id = id;
        this.crew_name = crew_name;
        this.user = user;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeValue() {
        return crew_name;
    }

    public void setCodeValue(String crew_name) {
        this.crew_name = crew_name;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getCreate() {
        return created;
    }

    public void setCreate(String created) {
        this.created = created;
    }


}
