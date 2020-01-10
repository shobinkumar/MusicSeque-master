package com.musicseque.venue_manager.model

import com.google.gson.annotations.SerializedName

class MySelectedTimeModel(

        @SerializedName("VenueId") val venue_id: String,
        @SerializedName("EventId") val event_id: String,
        @SerializedName("ArtistId") val artist_id: String,
        @SerializedName("VenueBookingDate") val venue_booking_date: String,
        @SerializedName("VenueBookingFrom") val venue_booking_from: String,
        @SerializedName("VenueBookingTo") val venue_booking_to: String,
        @SerializedName("BookingTime") val booking_time: String,
        @SerializedName("VenueBookingFromTime") val venue_booking_from_time: String,
        @SerializedName("VenueBookingToTime") val venue_booking_to_time: String,
        @SerializedName("BookingType") val booking_type: String,
        @SerializedName("BookingStatus") val booking_status: String,
        @SerializedName("BookedOn") val booked_on: String,
        @SerializedName("VenueStatus") val venue_status: String,

        @SerializedName("AvailabilityDate") val availability_date: String,
        @SerializedName("AvailabilityFromTime") val availability_from_time: String,
        @SerializedName("AvailabilityToTime") val availability_to_time: String,
        val isAvailable: Boolean = false

)