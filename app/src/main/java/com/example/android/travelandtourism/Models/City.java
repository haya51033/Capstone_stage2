
package com.example.android.travelandtourism.Models;
import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class City implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_En")
    @Expose
    private String nameEn;
    @SerializedName("name_Ar")
    @Expose
    private String nameAr;
    @SerializedName("description_En")
    @Expose
    private String descriptionEn;
    @SerializedName("description_Ar")
    @Expose
    private String descriptionAr;
    @SerializedName("images")
    @Expose
    private ArrayList<Images> images = null;
    @SerializedName("cityLocation")
    @Expose
    private String cityLocation;

    public Countries getCountries() {
        return countries;
    }

    public void setCountries(Countries countries) {
        this.countries = countries;
    }

    @SerializedName("country")
    @Expose
    private Countries countries;


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

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

}