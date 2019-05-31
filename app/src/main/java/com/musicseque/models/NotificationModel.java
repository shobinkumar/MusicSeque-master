package com.musicseque.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("LoggedInArtistId")
    @Expose
    private Integer loggedInArtistId;


    @SerializedName("ArtistFullName")
    @Expose
    private String artistFullName;





    @SerializedName("BandId")
    @Expose
    private Integer bandId;
    @SerializedName("BandName")
    @Expose
    private String bandName;
    @SerializedName("BandManagerId")
    @Expose
    private Integer bandManagerId;
    @SerializedName("BandManagerUserName")
    @Expose
    private String bandManagerUserName;
    @SerializedName("BandManagerFirstName")
    @Expose
    private String bandManagerFirstName;
    @SerializedName("BandManagerLastName")
    @Expose
    private String bandManagerLastName;
    @SerializedName("BandManagerEmailId")
    @Expose
    private String bandManagerEmailId;
    @SerializedName("BandManagerCity")
    @Expose
    private String bandManagerCity;
    @SerializedName("BandManagerPostcode")
    @Expose
    private String bandManagerPostcode;
    @SerializedName("RequestStatus")
    @Expose
    private String requestStatus;
    @SerializedName("LoggedInUserType")
    @Expose
    private String loggedInUserType;

    @SerializedName("RequestSentDate")
    @Expose
    private String requestSentDate;


    public Integer getLoggedInArtistId() {
        return loggedInArtistId;
    }

    public void setLoggedInArtistId(Integer loggedInArtistId) {
        this.loggedInArtistId = loggedInArtistId;
    }

    public Integer getBandId() {
        return bandId;
    }

    public void setBandId(Integer bandId) {
        this.bandId = bandId;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public Integer getBandManagerId() {
        return bandManagerId;
    }

    public void setBandManagerId(Integer bandManagerId) {
        this.bandManagerId = bandManagerId;
    }

    public String getBandManagerUserName() {
        return bandManagerUserName;
    }

    public void setBandManagerUserName(String bandManagerUserName) {
        this.bandManagerUserName = bandManagerUserName;
    }

    public String getBandManagerFirstName() {
        return bandManagerFirstName;
    }

    public void setBandManagerFirstName(String bandManagerFirstName) {
        this.bandManagerFirstName = bandManagerFirstName;
    }

    public String getBandManagerLastName() {
        return bandManagerLastName;
    }

    public void setBandManagerLastName(String bandManagerLastName) {
        this.bandManagerLastName = bandManagerLastName;
    }

    public String getBandManagerEmailId() {
        return bandManagerEmailId;
    }

    public void setBandManagerEmailId(String bandManagerEmailId) {
        this.bandManagerEmailId = bandManagerEmailId;
    }

    public String getBandManagerCity() {
        return bandManagerCity;
    }

    public void setBandManagerCity(String bandManagerCity) {
        this.bandManagerCity = bandManagerCity;
    }

    public String getBandManagerPostcode() {
        return bandManagerPostcode;
    }

    public void setBandManagerPostcode(String bandManagerPostcode) {
        this.bandManagerPostcode = bandManagerPostcode;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getLoggedInUserType() {
        return loggedInUserType;
    }

    public void setLoggedInUserType(String loggedInUserType) {
        this.loggedInUserType = loggedInUserType;
    }

    public String getArtistFullName() {
        return artistFullName;
    }

    public void setArtistFullName(String artistFullName) {
        this.artistFullName = artistFullName;
    }

    public String getRequestSentDate() {
        return requestSentDate;
    }

    public void setRequestSentDate(String requestSentDate) {
        this.requestSentDate = requestSentDate;
    }
}
