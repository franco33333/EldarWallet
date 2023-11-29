package com.example.eldarwallet.data.remote

import com.example.eldarwallet.data.remote.request.GenerateQRRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @POST("qr-code")
    @Headers(
        "Content-Type: application/json",
        "X-RapidAPI-Key: 6345193e77msh05622b1c233a472p123f6ejsn6da691b20a43",
        "X-RapidAPI-Host: neutrinoapi-qr-code.p.rapidapi.com"
    )
    suspend fun generateQR(@Body request: GenerateQRRequest): ResponseBody

}