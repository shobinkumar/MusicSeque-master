package com.musicseque.venue_manager.model

import com.google.gson.annotations.SerializedName

data class BookingStatusModel(

        @SerializedName("VenueId") val VenueId: Int,
        @SerializedName("EventId") val EventId: Int,
        @SerializedName("ArtistId") val ArtistId: Int,
        @SerializedName("ArtistName") val ArtistName: String,
        @SerializedName("VenueBookingFrom") val VenueBookingFrom: String,
        @SerializedName("VenueBookingTo") val VenueBookingTo: String,
        @SerializedName("BookingTime") val BookingTime: String,
        @SerializedName("VenueBookingFromTime") val VenueBookingFromTime: String,
        @SerializedName("VenueBookingToTime") val VenueBookingToTime: String,
        @SerializedName("BookingType") val BookingType: String,
        @SerializedName("BookingStatus") val BookingStatus: String,
        @SerializedName("BookedOn") val BookedOn: String,
        @SerializedName("VenueBookingId") val VenueBookingId: Int,

        @SerializedName("ArtistImgPath") val ArtistImgPath: String,
        @SerializedName("ArtistImg") val ArtistImg: String,
        @SerializedName("EventPromotionImgPath") val EventPromotionImgPath: String,
        @SerializedName("EventPromotionImg") val EventPromotionImg: String,
        @SerializedName("EventTitle") val EventName: String,


        @SerializedName("EventFromDate") val event_from_date: String,
        @SerializedName("EventToDate") val event_to_date: String,
        @SerializedName("EventTimeFrom") val event_time_from: String,
        @SerializedName("EventTimeTo") val event_time_to: String


)