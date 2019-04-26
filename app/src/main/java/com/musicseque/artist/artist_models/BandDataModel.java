package com.musicseque.artist.artist_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BandDataModel {
    @SerializedName("BandId")
    @Expose
    private Integer bandId;
    @SerializedName("UniqueCode")
    @Expose
    private String uniqueCode;
    @SerializedName("BandName")
    @Expose
    private String bandName;
    @SerializedName("BandContactNo")
    @Expose
    private String bandContactNo;
    @SerializedName("BandEmail")
    @Expose
    private String bandEmail;
    @SerializedName("BandWebsite")
    @Expose
    private String bandWebsite;
    @SerializedName("BandCity")
    @Expose
    private String bandCity;
    @SerializedName("PostCode")
    @Expose
    private String postCode;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("CountryName")
    @Expose
    private String countryName;
    @SerializedName("Bio")
    @Expose
    private String bio;
    @SerializedName("BandGenreId")
    @Expose
    private Integer bandGenreId;
    @SerializedName("GenreTypeName")
    @Expose
    private String genreTypeName;
    @SerializedName("ExperienceId")
    @Expose
    private Integer experienceId;
    @SerializedName("ExperienceYear")
    @Expose
    private String experienceYear;
    @SerializedName("Followers")
    @Expose
    private Integer followers;
    @SerializedName("Reviews")
    @Expose
    private Integer reviews;
    @SerializedName("NewStatus")
    @Expose
    private String newStatus;
    @SerializedName("BandImg")
    @Expose
    private String bandImg;
    @SerializedName("BandImgPath")
    @Expose
    private String bandImgPath;
    @SerializedName("NoOfBandMembers")
    @Expose
    private Integer noOfBandMembers;
    @SerializedName("BandManagerId")
    @Expose
    private Integer bandManagerId;
    @SerializedName("BandBackgroundImg")
    @Expose
    private String bandBackgroundImg;
    @SerializedName("BandBackgroundImgPath")
    @Expose
    private String bandBackgroundImgPath;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("LoginUserID")
    @Expose
    private Integer loginUserID;
    @SerializedName("ProfileTypeId")
    @Expose
    private Integer profileTypeId;

    public Integer getBandId() {
        return bandId;
    }

    public void setBandId(Integer bandId) {
        this.bandId = bandId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getBandContactNo() {
        return bandContactNo;
    }

    public void setBandContactNo(String bandContactNo) {
        this.bandContactNo = bandContactNo;
    }

    public String getBandEmail() {
        return bandEmail;
    }

    public void setBandEmail(String bandEmail) {
        this.bandEmail = bandEmail;
    }

    public String getBandWebsite() {
        return bandWebsite;
    }

    public void setBandWebsite(String bandWebsite) {
        this.bandWebsite = bandWebsite;
    }

    public String getBandCity() {
        return bandCity;
    }

    public void setBandCity(String bandCity) {
        this.bandCity = bandCity;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getBandGenreId() {
        return bandGenreId;
    }

    public void setBandGenreId(Integer bandGenreId) {
        this.bandGenreId = bandGenreId;
    }

    public String getGenreTypeName() {
        return genreTypeName;
    }

    public void setGenreTypeName(String genreTypeName) {
        this.genreTypeName = genreTypeName;
    }

    public Integer getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Integer experienceId) {
        this.experienceId = experienceId;
    }

    public String getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(String experienceYear) {
        this.experienceYear = experienceYear;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getBandImg() {
        return bandImg;
    }

    public void setBandImg(String bandImg) {
        this.bandImg = bandImg;
    }

    public String getBandImgPath() {
        return bandImgPath;
    }

    public void setBandImgPath(String bandImgPath) {
        this.bandImgPath = bandImgPath;
    }

    public Integer getNoOfBandMembers() {
        return noOfBandMembers;
    }

    public void setNoOfBandMembers(Integer noOfBandMembers) {
        this.noOfBandMembers = noOfBandMembers;
    }

    public Integer getBandManagerId() {
        return bandManagerId;
    }

    public void setBandManagerId(Integer bandManagerId) {
        this.bandManagerId = bandManagerId;
    }

    public String getBandBackgroundImg() {
        return bandBackgroundImg;
    }

    public void setBandBackgroundImg(String bandBackgroundImg) {
        this.bandBackgroundImg = bandBackgroundImg;
    }

    public String getBandBackgroundImgPath() {
        return bandBackgroundImgPath;
    }

    public void setBandBackgroundImgPath(String bandBackgroundImgPath) {
        this.bandBackgroundImgPath = bandBackgroundImgPath;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getLoginUserID() {
        return loginUserID;
    }

    public void setLoginUserID(Integer loginUserID) {
        this.loginUserID = loginUserID;
    }

    public Integer getProfileTypeId() {
        return profileTypeId;
    }

    public void setProfileTypeId(Integer profileTypeId) {
        this.profileTypeId = profileTypeId;
    }
}
