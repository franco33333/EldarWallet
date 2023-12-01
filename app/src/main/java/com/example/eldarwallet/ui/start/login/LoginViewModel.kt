package com.example.eldarwallet.ui.start.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.objects.User
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val _loginLiveData = MutableLiveData<User>()
    val loginLiveData: LiveData<User>
        get() = _loginLiveData

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            val db = EldarWalletApplication().getDatabase(getApplication())
            val userEntity = db.getItemsDao().getUser(userName, password)?.firstOrNull()
            if (userEntity == null) {
                onError.postValue(Throwable("error"))
            } else {
                val user = userEntity.toUser()
                AppPreferences.setUser(user)
                _loginLiveData.postValue(user)
            }
        }
    }
}