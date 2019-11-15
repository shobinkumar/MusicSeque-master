package com.musicseque.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("LoggedInUserId")
    @Expose
    private Integer loggedInUserId;

    @SerializedName("ArtistId")
    @Expose
    private Integer artist_id;


    @SerializedName("EventId")
    @Expose
    private Integer event_id;

    @SerializedName("Venue_BandId")
    @Expose
    private String venue_band_id;


    @SerializedName("ArtistFullName")
    @Expose
    private String artistFullName;


    @SerializedName("Venue_BandName")
    @Expose
    private String venue_band_name;

    @SerializedName("EventTitle")
    @Expose
    private Integer event_title;

    @SerializedName("Sender")
    @Expose
    private String sender;

    @SerializedName("IsRequestStatus")
    @Expose
    private String isRequestStatus;

    @SerializedName("CreatedDate")
    @Expose
    private String created_date;


    public Integer getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(Integer loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public Integer getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(Integer artist_id) {
        this.artist_id = artist_id;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public String getVenue_band_id() {
        return venue_band_id;
    }

    public void setVenue_band_id(String venue_band_id) {
        this.venue_band_id = venue_band_id;
    }

    public String getArtistFullName() {
        return artistFullName;
    }

    public void setArtistFullName(String artistFullName) {
        this.artistFullName = artistFullName;
    }

    public String getVenue_band_name() {
        return venue_band_name;
    }

    public void setVenue_band_name(String venue_band_name) {
        this.venue_band_name = venue_band_name;
    }

    public Integer getEvent_title() {
        return event_title;
    }

    public void setEvent_title(Integer event_title) {
        this.event_title = event_title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getIsRequestStatus() {
        return isRequestStatus;
    }

    public void setIsRequestStatus(String isRequestStatus) {
        this.isRequestStatus = isRequestStatus;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
