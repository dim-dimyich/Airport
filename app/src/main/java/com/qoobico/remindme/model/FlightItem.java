package com.qoobico.remindme.model;

/**
 * Created by Winner on 30.11.2015.
 */
public class FlightItem {
    String id, flight_number, flight_status, from_flight, to_flight, departure_datatime, arrival_datatime, flight_time, image;

    FlightCrew crew;
    public FlightItem(){

    }
    public FlightItem(String id, String flight_number, String flight_status,  String image, String from_flight, String to_flight, String departure_datatime, String arrival_datatime, String flight_time, FlightCrew crew){
        this.id = id;
        this.image = image;
        this.flight_number = flight_number;
        this.flight_status = flight_status;
        this.from_flight = from_flight;
        this.to_flight = to_flight;
        this.departure_datatime = departure_datatime;
        this.arrival_datatime = arrival_datatime;
        this.flight_time = flight_time;
        this.crew = crew;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return flight_number;
    }

    public void setNumber(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getStatus() {
        return flight_status;
    }

    public void setStatus(String flight_status) {
        this.flight_status = flight_status;
    }

    public String getFromFlight() {
        return from_flight;
    }

    public void setFromFlight(String from_flight) {
        this.from_flight = from_flight;
    }

    public String getToFlight() {
        return to_flight;
    }

    public void setToFlight(String to_flight) {
        this.to_flight = to_flight;
    }

    public String getDepTime() {
        return departure_datatime;
    }

    public void setDepTime(String departure_datatime) {
        this.departure_datatime = departure_datatime;
    }


    public String getArTime() {
        return arrival_datatime;
    }

    public void setArTime(String arrival_datatime) {
        this.arrival_datatime = arrival_datatime;
    }



    public String getFlightTime() {
        return flight_time;
    }

    public void setFlightTime(String flight_time) {
        this.flight_time = flight_time;
    }

    public FlightCrew getFlightcrew() {
        return crew;
    }

    public void setFlightcrew(FlightCrew crew) {
        this.crew = crew;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
