package com.example.eldarwallet.ui.main.generateQR

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.data.remote.ApiClient
import com.example.eldarwallet.data.remote.ApiClient.callApi
import com.example.eldarwallet.data.remote.request.GenerateQRRequest
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class GenerateQRViewModel(application: Application): BaseViewModel(application) {
    private val _qrGeneratedLiveData = MutableLiveData<String>()
    val qrGeneratedLiveData: LiveData<String>
        get() = _qrGeneratedLiveData

    fun generateQR(content: String) {
        viewModelScope.launch {
            ApiClient.service::generateQR.callApi(GenerateQRRequest(content)).collect {
                if (it.isSuccess)
                    it.getOrNull()?.let { url -> _qrGeneratedLiveData.postValue(url) }
                else
                    onError.postValue(it.exceptionOrNull())
            }
        }
    }
}