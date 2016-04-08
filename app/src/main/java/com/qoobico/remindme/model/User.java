package com.qoobico.remindme.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class User implements Serializable {
    String id, name, email, password, phone, readiness, user_image, position, costperhour, code_id, image;
    FlightCrew flightcrew;

    public User(){

    }
    public User(String id, String name, String email, String phone, String position, String user_image, String costperhour, String code_id){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.user_image = user_image;
        this.costperhour = costperhour;
        this.code_id = code_id;

    }
    public User(String name, String email, String position, String user_image, FlightCrew flightcrew) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.user_image = user_image;
        this.flightcrew = flightcrew;
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getReadiness() {
        return readiness;
    }

    public void setReadiness(String readiness) {
        this.readiness = readiness;
    }


    public String getImageUser() {
        return image;
    }
    public void setImageUser(String image) {
        this.image = image;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCost() {
        return costperhour;
    }

    public void setCost(String costperhour) {
        this.costperhour = costperhour;
    }

    public String getCodeId() {
        return code_id;
    }

    public void setCodeId(String code_id) {
        this.code_id = code_id;
    }

    public FlightCrew getFlightcrew() {
        return flightcrew;
    }

    public void setFlightcrew(FlightCrew flightcrew) {
        this.flightcrew = flightcrew;
    }

}