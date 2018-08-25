package com.example.android.travelandtourism.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class WidgetInfo implements Parcelable, Serializable {
    private Integer id= null;
    private String FlightDestenationCity = null;
    private String FlightTime = null;
    private String HotelName = null;
    private String CheckInDate = null;
    public WidgetInfo(){}

    public WidgetInfo(Integer id, String flightDestenationCity, String flightTime, String hotelName, String checkInDate) {
        this.id = id;
        FlightDestenationCity = flightDestenationCity;
        FlightTime = flightTime;
        HotelName = hotelName;
        CheckInDate = checkInDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlightDestenationCity() {
        return FlightDestenationCity;
    }

    public void setFlightDestenationCity(String flightDestenationCity) {
        FlightDestenationCity = flightDestenationCity;
    }

    public String getFlightTime() {
        return FlightTime;
    }

    public void setFlightTime(String flightTime) {
        FlightTime = flightTime;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    protected WidgetInfo(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        FlightDestenationCity = in.readString();
        FlightTime = in.readString();
        HotelName = in.readString();
        CheckInDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(FlightDestenationCity);
        dest.writeString(FlightTime);
        dest.writeString(HotelName);
        dest.writeString(CheckInDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WidgetInfo> CREATOR = new Parcelable.Creator<WidgetInfo>() {
        @Override
        public WidgetInfo createFromParcel(Parcel in) {
            return new WidgetInfo(in);
        }

        @Override
        public WidgetInfo[] newArray(int size) {
            return new WidgetInfo[size];
        }
    };
}