package com.musicseque.event_manager.model

import com.google.gson.annotations.SerializedName

data class EventListModel(

        @SerializedName("EventManagerId") val event_manager_id: String,
        @SerializedName("EventId") val event_id: String,
        @SerializedName("EventTitle") val event_title: String,
        @SerializedName("EventDescription") val event_description: String,
        @SerializedName("EventTypeId") val event_type_id: String,
        @SerializedName("EventTypeName") val event_type_name: String,
        @SerializedName("EventFromDate") val event_from_date: String,
        @SerializedName("EventToDate") val event_to_date: String,
        @SerializedName("EventTimeFrom") val event_time_from: String,
        @SerializedName("EventTimeTo") val event_time_to: String,

        @SerializedName("EventCurrencyId") val event_currency_id: String,
        @SerializedName("EventCurrency") val event_currency: String,
        @SerializedName("EventBudget") val event_budget: String,
        @SerializedName("EventEstimatedGuest") val event_estimated_guest: String,
        @SerializedName("EventPromotionImg") val event_promotion_image: String,
        @SerializedName("EventPromotionImgPath") val event_promotion_path: String,
        @SerializedName("EventStatus") val event_status: String

        )
