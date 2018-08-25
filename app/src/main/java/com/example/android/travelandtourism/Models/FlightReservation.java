package com.example.android.travelandtourism.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class FlightReservation  implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer")
    @Expose
    private UserModel customer;
    @SerializedName("flight")
    @Expose
    private Flight flight;
    @SerializedName("seats")
    @Expose
    private Integer seats;
    @SerializedName("flightClass")
    @Expose
    private String flightClass;
    @SerializedName("reservationPrice")
    @Expose
    private Integer reservationPrice;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("displayDateTime")
    @Expose
    private String displayDateTime;
    @SerializedName("isBooked")
    @Expose
    private Boolean isBooked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserModel customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public Integer getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(Integer reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

}
