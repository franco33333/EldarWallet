package com.example.eldarwallet.ui.main.scanQR

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.utils.BaseViewModel

class ScanViewModel(application: Application): BaseViewModel(application) {
    private val _payGeneratedLiveData = MutableLiveData<Unit>()
    val payGeneratedLiveData: LiveData<Unit>
        get() = _payGeneratedLiveData

    fun pay(amount: Long) {
        val user = AppPreferences.getUser()!!
        user.balance -= amount
        AppPreferences.setUser(user)

        _payGeneratedLiveData.postValue(Unit)
    }
}