package com.example.eldarwallet.data.local.objects

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("surname")
    var surname: String? = null,
    @SerializedName("user_name")
    var userName: String? = null,
    @SerializedName("password")
    var password: String? = null
) {
    @SerializedName("cards")
    var cards: MutableList<Card>? = mutableListOf()
    @SerializedName("balance")
    var balance: Long = 100000
    @SerializedName("id")
    var id: Long? = null
    @SerializedName("qr")
    var qr: Bitmap? = null
}