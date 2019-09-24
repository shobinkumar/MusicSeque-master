package com.musicseque.venue_manager.model

import com.google.gson.annotations.SerializedName

data class VenueSearchModel (@SerializedName("LoggedInUserId") val loggedInUserId: String,
                             @SerializedName("SearchText") val searchText: String,
                             @SerializedName("VenueId") val venueId: String,
                             @SerializedName("VenueName") val venueName: String,
                             @SerializedName("VenueProfileImg") val venueProfileImg: String,
                             @SerializedName("VenueProfileImgPath") val venueProfileImgPath: String,
                             @SerializedName("VenueManagerId") val venueManagerId: String,
                             @SerializedName("VenueManagerFullName") val venueManagerFullName: String,
                             @SerializedName("VenueManagerProfileImg") val venueManagerProfileImg: String,
                             @SerializedName("VenueManagerSocialImgUrl") val venueManagerSocialImgUrl: String,
                             @SerializedName("VenueManagerProfileImgPath") val venueManagerProfileImgPath: String,
                             @SerializedName("VenueCountryId") val venueCountryId: String,
                             @SerializedName("VenueCountry") val venueCountry: String,
                             @SerializedName("VenueAddress") val venueAddress: String,
                             @SerializedName("VenueCity") val venueCity: String)