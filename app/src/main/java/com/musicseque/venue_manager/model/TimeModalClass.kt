package com.musicseque.venue_manager.model

import com.google.gson.annotations.SerializedName

data class TimeModalClass (
        @SerializedName("start_time") val start_time: String,
        @SerializedName("end_time") val end_time: String

)