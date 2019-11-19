package com.musicseque.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("LoggedInUserId")
    @Expose
    private int loggedInUserId;

    @SerializedName("ArtistId")
    @Expose
    private int artist_id;


    @SerializedName("EventId")
    @Expose
    private int event_id;

    @SerializedName("Venue_BandId")
    @Expose
    private int venue_band_id;


    @SerializedName("ArtistFullName")
    @Expose
    private String artistFullName;


    @SerializedName("Venue_BandName")
    @Expose
    private String venue_band_name;

    @SerializedName("EventTitle")
    @Expose
    private String event_title;

    @SerializedName("Sender")
    @Expose
    private int sender;

    @SerializedName("IsRequestStatus")
    @Expose
    private int isRequestStatus;

    @SerializedName("CreatedDate")
    @Expose
    private String created_date;


    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getVenue_band_id() {
        return venue_band_id;
    }

    public void setVenue_band_id(int venue_band_id) {
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

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getIsRequestStatus() {
        return isRequestStatus;
    }

    public void setIsRequestStatus(int isRequestStatus) {
        this.isRequestStatus = isRequestStatus;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
