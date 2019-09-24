package com.musicseque.venue_manager.model

import com.google.gson.annotations.SerializedName

class TimmingModel(
        @SerializedName("Time") val loggedInUserId: String,
        @SerializedName("Id") val searchText: String,
        @SerializedName("VenueId") val venueId: String,
        @SerializedName("VenueName") val venueName: String
        )