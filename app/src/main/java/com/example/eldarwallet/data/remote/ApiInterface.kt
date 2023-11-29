package com.example.eldarwallet.data.remote

import com.example.eldarwallet.data.remote.request.GenerateQRRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "X-RapidAPI-Key: e08f32cc30mshfe214c39444f978p154837jsn0aecb6d04d82",
        "X-RapidAPI-Host: neutrinoapi-qr-code.p.rapidapi.com"
    )
    @POST("qr-code")
    suspend fun generateQR(@Body request: GenerateQRRequest): String

}