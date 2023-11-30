package com.example.eldarwallet.ui.start.signUp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.objects.User
import com.example.eldarwallet.data.local.room.UserEntity
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application): BaseViewModel(application) {
    private val _signUpLiveData = MutableLiveData<User>()
    val signUpLiveData: LiveData<User>
        get() = _signUpLiveData

    fun signUp(name: String, surname: String, userName: String, password: String) {
        viewModelScope.launch {
            val db = EldarWalletApplication().getDatabase(getApplication())
            db.getItemsDao().insertUser(UserEntity(0,
                name, surname, userName, password, 100000))
            val user = User(name, surname, userName, password)
            user.balance = 100000
            AppPreferences.setUser(user)
            _signUpLiveData.postValue(user)
        }
    }
}