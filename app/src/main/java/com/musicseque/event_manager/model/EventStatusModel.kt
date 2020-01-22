package com.musicseque.event_manager.model

import com.google.gson.annotations.SerializedName

data class EventStatusModel(
        @SerializedName("EventType") val name: String,
        @SerializedName("Type") val type: Int
)