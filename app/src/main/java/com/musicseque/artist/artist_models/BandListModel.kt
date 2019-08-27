package com.musicseque.artist.artist_models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class BandListModel {

    @SerializedName("LoggedInUserId")
    @Expose
    private var loggedInUserId: Int? = null
    @SerializedName("SearchText")
    @Expose
    private var searchText: Any? = null
    @SerializedName("BandId")
    @Expose
    private var bandId: Int? = null
    @SerializedName("BandUniqueCode")
    @Expose
    private var bandUniqueCode: String? = null
    @SerializedName("BandName")
    @Expose
    private var bandName: String? = null
    @SerializedName("BandProfileImg")
    @Expose
    private var bandProfileImg: String? = null
    @SerializedName("BandProfileImgPath")
    @Expose
    private var bandProfileImgPath: String? = null
    @SerializedName("BandManagerId")
    @Expose
    private var bandManagerId: Int? = null
    @SerializedName("BandManagerFullName")
    @Expose
    private var bandManagerFullName: String? = null
    @SerializedName("BandManagerProfileImg")
    @Expose
    private var bandManagerProfileImg: String? = null
    @SerializedName("BandManagerProfileImgPath")
    @Expose
    private var bandManagerProfileImgPath: String? = null
    @SerializedName("BandManagerSocialImgUrl")
    @Expose
    private var bandManagerSocialImgUrl: String? = null
    @SerializedName("BandCountryId")
    @Expose
    private var bandCountryId: Int? = null
    @SerializedName("BandCountry")
    @Expose
    private var bandCountry: String? = null
    @SerializedName("BandCity")
    @Expose
    private var bandCity: String? = null
    @SerializedName("BandGenreId")
    @Expose
    private var bandGenreId: Int? = null
    @SerializedName("BandGenreType")
    @Expose
    private var bandGenreType: String? = null

    fun getLoggedInUserId(): Int? {
        return loggedInUserId
    }

    fun setLoggedInUserId(loggedInUserId: Int?) {
        this.loggedInUserId = loggedInUserId
    }

    fun getSearchText(): Any? {
        return searchText
    }

    fun setSearchText(searchText: Any) {
        this.searchText = searchText
    }

    fun getBandId(): Int? {
        return bandId
    }

    fun setBandId(bandId: Int?) {
        this.bandId = bandId
    }

    fun getBandUniqueCode(): String? {
        return bandUniqueCode
    }

    fun setBandUniqueCode(bandUniqueCode: String) {
        this.bandUniqueCode = bandUniqueCode
    }

    fun getBandName(): String? {
        return bandName
    }

    fun setBandName(bandName: String) {
        this.bandName = bandName
    }

    fun getBandProfileImg(): String? {
        return bandProfileImg
    }

    fun setBandProfileImg(bandProfileImg: String) {
        this.bandProfileImg = bandProfileImg
    }

    fun getBandProfileImgPath(): String? {
        return bandProfileImgPath
    }

    fun setBandProfileImgPath(bandProfileImgPath: String) {
        this.bandProfileImgPath = bandProfileImgPath
    }

    fun getBandManagerId(): Int? {
        return bandManagerId
    }

    fun setBandManagerId(bandManagerId: Int?) {
        this.bandManagerId = bandManagerId
    }

    fun getBandManagerFullName(): String? {
        return bandManagerFullName
    }

    fun setBandManagerFullName(bandManagerFullName: String) {
        this.bandManagerFullName = bandManagerFullName
    }

    fun getBandManagerProfileImg(): String? {
        return bandManagerProfileImg
    }

    fun setBandManagerProfileImg(bandManagerProfileImg: String) {
        this.bandManagerProfileImg = bandManagerProfileImg
    }

    fun getBandManagerProfileImgPath(): String? {
        return bandManagerProfileImgPath
    }

    fun setBandManagerProfileImgPath(bandManagerProfileImgPath: String) {
        this.bandManagerProfileImgPath = bandManagerProfileImgPath
    }

    fun getBandManagerSocialImgUrl(): String? {
        return bandManagerSocialImgUrl
    }

    fun setBandManagerSocialImgUrl(bandManagerSocialImgUrl: String) {
        this.bandManagerSocialImgUrl = bandManagerSocialImgUrl
    }

    fun getBandCountryId(): Int? {
        return bandCountryId
    }

    fun setBandCountryId(bandCountryId: Int?) {
        this.bandCountryId = bandCountryId
    }

    fun getBandCountry(): String? {
        return bandCountry
    }

    fun setBandCountry(bandCountry: String) {
        this.bandCountry = bandCountry
    }

    fun getBandCity(): String? {
        return bandCity
    }

    fun setBandCity(bandCity: String) {
        this.bandCity = bandCity
    }

    fun getBandGenreId(): Int? {
        return bandGenreId
    }

    fun setBandGenreId(bandGenreId: Int?) {
        this.bandGenreId = bandGenreId
    }

    fun getBandGenreType(): String? {
        return bandGenreType
    }

    fun setBandGenreType(bandGenreType: String) {
        this.bandGenreType = bandGenreType
    }
}