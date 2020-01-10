package com.musicseque.artist.artist_models

import com.google.gson.annotations.SerializedName

class GigsModel(
        @SerializedName("EventId") val event_id: String,
        @SerializedName("EventTitle") val event_title: String,


        @SerializedName("EventDescription") val event_description: String,
        @SerializedName("EventTypeId") val event_type_id: String,

        @SerializedName("EventTypeName") val event_type_name: String,
        @SerializedName("EventDateFrom") val event_from_date: String,
        @SerializedName("EventDateTo") val event_to_date: String,
        @SerializedName("EventTimeFrom") val event_time_from: String,
        @SerializedName("EventTimeTo") val event_time_to: String,

        @SerializedName("EventBudget") val event_budget: String,
        @SerializedName("EventChargesPayCurrencyId") val event_currency_id: String,
        @SerializedName("EventChargesPayCurrency") val event_currency: String,
        @SerializedName("EventEstimatedGuest") val event_estimated_guest: String,
        @SerializedName("EventStatus") val event_status: String,
        @SerializedName("EventManagerId") val event_manager_id: String,
        @SerializedName("EventManagerName") val event_manager_name: String,


        @SerializedName("EventPromotionImg") val event_promotion_image: String,
        @SerializedName("EventPromotionImgPath") val event_promotion_path: String,


        @SerializedName("VenueName") val venue_name: String,
        @SerializedName("VenueBookedFromDate") val venue_from_date: String,
        @SerializedName("VenueBookedToDate") val venue_to_date: String,
        @SerializedName("VenueBookingFromTime") val venue_from_time: String,
        @SerializedName("VenueBookingToTime") val venue_to_time: String,
        @SerializedName("VenueBookingId") val venue_booking_id: String,
        @SerializedName("EMUsername") val event_manager_email_id: String,
        @SerializedName("EMPhone") val event_manager_phone: String,
        @SerializedName("VenueBookingStatus") val venue_booking_status: String,
        @SerializedName("LoggedInUserId") val logged_user_id: String

)