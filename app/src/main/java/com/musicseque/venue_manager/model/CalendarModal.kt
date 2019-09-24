package com.musicseque.venue_manager.model

import com.google.gson.annotations.SerializedName

data class CalendarModal(

        @SerializedName("intDay") val intDay: String,
        @SerializedName("fullDate") val fullDate: String,
        @SerializedName("dateString") val dateString: String,
        @SerializedName("isCurrentDate") var isCurrentDate: Boolean
)