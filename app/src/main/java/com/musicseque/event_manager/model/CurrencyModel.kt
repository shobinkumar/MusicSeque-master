package com.musicseque.event_manager.model

import com.google.gson.annotations.SerializedName

data class CurrencyModel(
        @SerializedName("EventPayCurrencyId") val id: String,
        @SerializedName("EventCurrency") val currency: String,
        @SerializedName("EventPayCurrencyType") val currencyType: String,
        @SerializedName("EventPayCurrencySymbol") val currencySymbol: String
)