package com.example.android.travelandtourism.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Flight implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sourceCity")
    @Expose
    private City sourceCity;
    @SerializedName("destinationCity")
    @Expose
    private City destinationCity;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("displayDate")
    @Expose
    private String displayDate;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("ecoSeatsCount")
    @Expose
    private  Integer ecoSeatsCount;
    @SerializedName("frstSeatsCount")
    @Expose
    private  Integer frstSeatsCount;
    @SerializedName("flightDuration")
    @Expose
    private String flightDuration;
    @SerializedName("economyTicketPrice")
    @Expose
    private Integer economyTicketPrice;
    @SerializedName("firstClassTicketPrice")
    @Expose
    private Integer firstClassTicketPrice;
    @SerializedName("airline")
    @Expose
    private String airline;
    /*
    * @SerializedName("reservations")
    @Expose
    private Object reservations;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public City getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(City sourceCity) {
        this.sourceCity = sourceCity;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getEcoSeatsCount() {
        return ecoSeatsCount;
    }

    public void setEcoSeatsCount(Integer ecoSeatsCount) {
        this.ecoSeatsCount = ecoSeatsCount;
    }

    public Integer getFrstSeatsCount() {
        return frstSeatsCount;
    }

    public void setFrstSeatsCount(Integer frstSeatsCount) {
        this.frstSeatsCount = frstSeatsCount;
    }


    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Integer getEconomyTicketPrice() {
        return economyTicketPrice;
    }

    public void setEconomyTicketPrice(Integer economyTicketPrice) {
        this.economyTicketPrice = economyTicketPrice;
    }

    public Integer getFirstClassTicketPrice() {
        return firstClassTicketPrice;
    }

    public void setFirstClassTicketPrice(Integer firstClassTicketPrice) {
        this.firstClassTicketPrice = firstClassTicketPrice;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

   /*
   *  public Object getReservations() {
        return reservations;
    }

    public void setReservations(Object reservations) {
        this.reservations = reservations;
    }*/

}