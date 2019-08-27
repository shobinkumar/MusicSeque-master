package com.musicseque.event_manager.model

import com.google.gson.annotations.SerializedName

class EventModel(

        @SerializedName("EventTypeId") val id: String,
        @SerializedName("EventType") val name: String,
        var isSelected: Boolean=false

)