package com.example.android.travelandtourism.Models;

/**
 * Created by haya on 02/09/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

public class HotelRoom implements Serializable {
    public HotelRoom(Integer id, Room room, Integer count, Integer nightPrice) {
        this.id = id;
        this.room = room;
        this.count = count;
        this.nightPrice = nightPrice;
    }

    public HotelRoom() {

    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("room")
    @Expose
    private Room room;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("nightPrice")
    @Expose
    private Integer nightPrice;
    @SerializedName("hotel")
    @Expose
    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNightPrice() {
        return nightPrice;
    }

    public void setNightPrice(Integer nightPrice) {
        this.nightPrice = nightPrice;
    }

}
