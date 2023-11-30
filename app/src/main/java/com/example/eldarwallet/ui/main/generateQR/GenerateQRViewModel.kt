package com.example.eldarwallet.ui.main.generateQR

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.remote.ApiClient
import com.example.eldarwallet.data.remote.ApiClient.callApi
import com.example.eldarwallet.data.remote.request.GenerateQRRequest
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class GenerateQRViewModel(application: Application): BaseViewModel(application) {
    private val _qrGeneratedLiveData = MutableLiveData<Bitmap>()
    val qrGeneratedLiveData: LiveData<Bitmap>
        get() = _qrGeneratedLiveData

    fun generateQR(content: String) {
        viewModelScope.launch {
            ApiClient.service::generateQR.callApi(GenerateQRRequest(content)).collect {
                if (it.isSuccess)
                    it.getOrNull()?.let { response ->
                        val imageBytes: ByteArray = response.bytes()
                        // Decodifica los bytes en un objeto Bitmap
                        val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        val user = AppPreferences.getUser()!!
                        user.qr = bitmap
                        AppPreferences.setUser(user)
                        _qrGeneratedLiveData.postValue(bitmap)
                    }
                else
                    onError.postValue(it.exceptionOrNull())
            }
        }
    }
}