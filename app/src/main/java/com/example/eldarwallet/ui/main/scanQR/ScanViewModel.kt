package com.example.eldarwallet.ui.main.scanQR

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.room.UserEntity
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class ScanViewModel(application: Application) : BaseViewModel(application) {
    private val _payGeneratedLiveData = MutableLiveData<Unit>()
    val payGeneratedLiveData: LiveData<Unit>
        get() = _payGeneratedLiveData

    fun pay(amount: Long) {
        viewModelScope.launch {
            val user = AppPreferences.getUser()!!

            val db = EldarWalletApplication().getDatabase(getApplication())
            db.getItemsDao().insertUser(
                UserEntity(
                    user.id!!, user.name!!, user.surname!!, user.userName!!,
                    user.password!!, user.balance - amount
                )
            )

            user.balance -= amount
            AppPreferences.setUser(user)

            _payGeneratedLiveData.postValue(Unit)
        }
    }
}