package com.example.android.travelandtourism.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Offer implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("flightReservations")
    @Expose
    private ArrayList<FlightReservation> flightReservations = null;
    @SerializedName("flightBackReservations")
    @Expose
    private ArrayList<FlightReservation> flightBackReservations = null;
    @SerializedName("hotelReservations")
    @Expose
    private ArrayList<HotelReservations> hotelReservations = null;
    @SerializedName("customersCount")
    @Expose
    private Integer customersCount;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("newPrice")
    @Expose
    private Integer newPrice;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("details_En")
    @Expose
    private String detailsEn;
    @SerializedName("details_Ar")
    @Expose
    private String detailsAr;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("status")
    @Expose
    private String status;

    public Offer(Integer id, ArrayList<FlightReservation> flightReservations,
                 ArrayList<FlightReservation> flightBackReservations, ArrayList<HotelReservations> hotelReservations,
                 Integer customersCount, Double discount, Integer price, Integer newPrice, Integer count,
                 String detailsEn, String detailsAr, Integer duration, String status) {
        this.id = id;
        this.flightReservations = flightReservations;
        this.flightBackReservations = flightBackReservations;
        this.hotelReservations = hotelReservations;
        this.customersCount = customersCount;
        this.discount = discount;
        this.price = price;
        this.newPrice = newPrice;
        this.count = count;
        this.detailsEn = detailsEn;
        this.detailsAr = detailsAr;
        this.duration = duration;
        this.status = status;
    }

    public Offer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(ArrayList<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }

    public ArrayList<FlightReservation> getFlightBackReservations() {
        return flightBackReservations;
    }

    public void setFlightBackReservations(ArrayList<FlightReservation> flightBackReservations) {
        this.flightBackReservations = flightBackReservations;
    }

    public ArrayList<HotelReservations> getHotelReservations() {
        return hotelReservations;
    }

    public void setHotelReservations(ArrayList<HotelReservations> hotelReservations) {
        this.hotelReservations = hotelReservations;
    }

    public Integer getCustomersCount() {
        return customersCount;
    }

    public void setCustomersCount(Integer customersCount) {
        this.customersCount = customersCount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Integer newPrice) {
        this.newPrice = newPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public void setDetailsEn(String detailsEn) {
        this.detailsEn = detailsEn;
    }

    public String getDetailsAr() {
        return detailsAr;
    }

    public void setDetailsAr(String detailsAr) {
        this.detailsAr = detailsAr;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
