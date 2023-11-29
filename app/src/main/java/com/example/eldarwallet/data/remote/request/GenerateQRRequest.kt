package com.example.eldarwallet.data.remote.request

import com.google.gson.annotations.SerializedName

data class GenerateQRRequest(
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("width")
    val width: Int = 256,
    @SerializedName("height")
    val height: Int = 256
)
