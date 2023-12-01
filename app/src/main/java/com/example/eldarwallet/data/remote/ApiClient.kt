package com.example.eldarwallet.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KSuspendFunction1


object ApiClient {

    var service = createService()

    private fun createService(): ApiInterface {
        val httpClient = getOkHttpClient()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val sRestAdapter = Retrofit.Builder()
            .baseUrl(
                "https://neutrinoapi-qr-code.p.rapidapi.com/"
            )
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()

        return sRestAdapter.create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(HttpLoggingInterceptor { message ->
            Log.d("ApiClient", message)
        }.apply { level = HttpLoggingInterceptor.Level.BODY })

        val timeout = 30L
        return builder
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }

    fun <REQUEST, RESPONSE> KSuspendFunction1<REQUEST, RESPONSE>.callApi(request: REQUEST) =
        flow {
            try {
                val response = this@callApi.invoke(request)
                emit(Result.success(response))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
}