package com.example.eldarwallet.ui.start.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eldarwallet.data.local.AppPreferences

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val _loginLiveData = MutableLiveData<Boolean>()
    val loginLiveData: LiveData<Boolean>
        get() = _loginLiveData

    fun login(userName: String, password: String) {
        val userRegistered = AppPreferences.getUser()
        if(userRegistered != null && userRegistered.userName == userName && userRegistered.password == password) {
            _loginLiveData.postValue(true)
            AppPreferences.setUser(userRegistered)
        } else {
            _loginLiveData.postValue(false)
        }
    }
}