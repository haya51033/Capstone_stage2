package com.example.android.travelandtourism.Models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class Hotel  implements Serializable{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_En")
    @Expose
    private String nameEn;
    @SerializedName("name_Ar")
    @Expose
    private String nameAr;
    @SerializedName("stars")
    @Expose
    private Integer stars;
    @SerializedName("details_En")
    @Expose
    private String detailsEn;
    @SerializedName("details_Ar")
    @Expose
    private String detailsAr;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("gpsX")
    @Expose
    private String gpsX;
    @SerializedName("gpsY")
    @Expose
    private String gpsY;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("images")
    @Expose

    private ArrayList<Images> images = null;
    @SerializedName("hotelRooms")
    @Expose

    private ArrayList<HotelRoom> hotelRooms = null;
    @SerializedName("hotelRates")
    @Expose
    private ArrayList<HotelRate> hotelRates = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public ArrayList<HotelRoom> getHotelRooms() {
        return hotelRooms;
    }

    public void setHotelRooms(ArrayList<HotelRoom> hotelRooms) {
        this.hotelRooms = hotelRooms;
    }

    public ArrayList<HotelRate> getHotelRates() {
        return hotelRates;
    }

    public void setHotelRates(ArrayList<HotelRate> hotelRates) {
        this.hotelRates = hotelRates;
    }
    public String getGpsX() {
        return gpsX;
    }

    public void setGpsX(String gpsX) {
        this.gpsX = gpsX;
    }

    public String getGpsY() {
        return gpsY;
    }

    public void setGpsY(String gpsy) {
        this.gpsY = gpsy;
    }

}
