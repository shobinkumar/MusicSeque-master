package com.musicseque.event_manager.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventModelParcelable(
        val EventId: String,
        val EventTitle: String,
        val EventDescription: String,
        val EventTypeId: String,
        val EventDateFrom: String,
        val EventDateTo: String,
        val EventTimeFrom: String,
        val EventTimeTo: String,
        val EventGatheringCapacity: String,
        val EventChargesPayCurrencyId: String,
        val EventBudget: String

):Parcelable {

}

