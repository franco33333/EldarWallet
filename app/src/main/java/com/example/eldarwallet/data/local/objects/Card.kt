package com.example.eldarwallet.data.local.objects

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("number")
    var number: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("expiration_date")
    var expirationDate: String? = null,
    @SerializedName("security_code")
    var securityCode: String? = null,
    @SerializedName("document")
    var document: String? = null,
    var isSelected: Boolean = false
)