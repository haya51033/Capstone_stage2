package com.example.android.travelandtourism.Models;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.android.travelandtourism.Activities.ResponseValue;





public class Countries  implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_En")
    @Expose
    private String nameEn;
    @SerializedName("name_Ar")
    @Expose
    private String nameAr;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("visible")
    @Expose
    private Boolean visible;
    @SerializedName("cities")
    @Expose

    public ArrayList<City> cities = null;

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public Countries(Integer id, String nameEn, String nameAr, String flag, Boolean visible, ArrayList<City> cities) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.flag = flag;
        this.visible = visible;
        this.cities = cities;
    }

    public Countries() {

    }
}
