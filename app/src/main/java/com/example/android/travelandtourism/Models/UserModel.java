package com.example.android.travelandtourism.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class UserModel implements Serializable {

    @SerializedName("claims")
    @Expose

    private ArrayList<Claims> claims = null;
    @SerializedName("logins")
    @Expose

    private ArrayList<Logins> logins = null;
    @SerializedName("roles")
    @Expose

    private ArrayList<Roles> roles = null;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("registrationDateTime")
    @Expose
    private String registrationDateTime;
    @SerializedName("lastLoginDateTime")
    @Expose
    private String lastLoginDateTime;
    @SerializedName("loginCount")
    @Expose
    private Integer loginCount;
    @SerializedName("credit")
    @Expose
    private Double credit;
    @SerializedName("isAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("preferredInterfaceLanguage")
    @Expose
    private String preferredInterfaceLanguage;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("passwordHash")
    @Expose
    private String passwordHash;
    @SerializedName("securityStamp")
    @Expose
    private String securityStamp;

    @SerializedName("succeeded")
    @Expose
    private Boolean succeeded;

    @SerializedName("errors")
    @Expose

    private ArrayList<Errors> errors;

    public ArrayList<Errors> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<Errors> errors) {
        this.errors = errors;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Boolean succeeded) {
        this.succeeded = succeeded;
    }


    public ArrayList<Claims> getClaims() {
        return claims;
    }

    public void setClaims(ArrayList<Claims> claims) {
        this.claims = claims;
    }

    public ArrayList<Logins> getLogins() {
        return logins;
    }

    public void setLogins(ArrayList<Logins> logins) {
        this.logins = logins;
    }

    public ArrayList<Roles> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Roles> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(String registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    public String getLastLoginDateTime() {
        return lastLoginDateTime;
    }

    public void setLastLoginDateTime(String lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPreferredInterfaceLanguage() {
        return preferredInterfaceLanguage;
    }

    public void setPreferredInterfaceLanguage(String preferredInterfaceLanguage) {
        this.preferredInterfaceLanguage = preferredInterfaceLanguage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSecurityStamp() {
        return securityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        this.securityStamp = securityStamp;
    }

}