package com.example.android.travelandtourism.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HotelReservations  implements Serializable, Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("guest")
    @Expose
    private UserModel guest;
    @SerializedName("room")
    @Expose
    private HotelRoom room;
    @SerializedName("check_In_Date")
    @Expose
    private String checkInDate;
    @SerializedName("displayCheck_In_Date")
    @Expose
    private String displayCheckInDate;
    @SerializedName("check_Out_Date")
    @Expose
    private String checkOutDate;
    @SerializedName("displayCheck_Out_Date")
    @Expose
    private String displayCheckOutDate;
    @SerializedName("reservationCost")
    @Expose
    private Integer reservationCost;
    @SerializedName("roomsAvailable")
    @Expose
    private Integer roomsAvailable;
    @SerializedName("isBooked")
    @Expose
    private Boolean isBooked;


    public HotelReservations() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getGuest() {
        return guest;
    }

    public void setGuest(UserModel guest) {
        this.guest = guest;
    }

    public HotelRoom getRoom() {
        return room;
    }

    public void setRoom(HotelRoom room) {
        this.room = room;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getDisplayCheckInDate() {
        return displayCheckInDate;
    }

    public void setDisplayCheckInDate(String displayCheckInDate) {
        this.displayCheckInDate = displayCheckInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getDisplayCheckOutDate() {
        return displayCheckOutDate;
    }

    public void setDisplayCheckOutDate(String displayCheckOutDate) {
        this.displayCheckOutDate = displayCheckOutDate;
    }

    public Integer getReservationCost() {
        return reservationCost;
    }

    public void setReservationCost(Integer reservationCost) {
        this.reservationCost = reservationCost;
    }

    public Integer getRoomsAvailable() {
        return roomsAvailable;
    }

    public void setRoomsAvailable(Integer roomsAvailable) {
        this.roomsAvailable = roomsAvailable;
    }

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }


    protected HotelReservations(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        guest = (UserModel) in.readValue(UserModel.class.getClassLoader());
        room = (HotelRoom) in.readValue(HotelRoom.class.getClassLoader());
        checkInDate = in.readString();
        displayCheckInDate = in.readString();
        checkOutDate = in.readString();
        displayCheckOutDate = in.readString();
        reservationCost = in.readByte() == 0x00 ? null : in.readInt();
        roomsAvailable = in.readByte() == 0x00 ? null : in.readInt();
        byte isBookedVal = in.readByte();
        isBooked = isBookedVal == 0x02 ? null : isBookedVal != 0x00;
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
        dest.writeValue(guest);
        dest.writeValue(room);
        dest.writeString(checkInDate);
        dest.writeString(displayCheckInDate);
        dest.writeString(checkOutDate);
        dest.writeString(displayCheckOutDate);
        if (reservationCost == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(reservationCost);
        }
        if (roomsAvailable == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(roomsAvailable);
        }
        if (isBooked == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isBooked ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HotelReservations> CREATOR = new Parcelable.Creator<HotelReservations>() {
        @Override
        public HotelReservations createFromParcel(Parcel in) {
            return new HotelReservations(in);
        }

        @Override
        public HotelReservations[] newArray(int size) {
            return new HotelReservations[size];
        }
    };
}