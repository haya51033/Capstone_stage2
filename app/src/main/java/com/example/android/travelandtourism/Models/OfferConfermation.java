package com.example.android.travelandtourism.Models;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OfferConfermation  implements Serializable {


        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("customer")
        @Expose
        private UserModel customer;
        @SerializedName("offer")
        @Expose
        private Offer offer;
        @SerializedName("dateTime")
        @Expose
        private String dateTime;

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

        public Offer getOffer() {
            return offer;
        }

        public void setOffer(Offer offer) {
            this.offer = offer;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }



}
