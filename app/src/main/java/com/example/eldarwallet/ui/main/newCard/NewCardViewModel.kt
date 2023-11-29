package com.example.eldarwallet.ui.main.newCard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.objects.Card

class NewCardViewModel(application: Application): AndroidViewModel(application) {
    private val _cardCreatedLiveData = MutableLiveData<Any>()
    val cardCreatedLiveData: LiveData<Any>
        get() = _cardCreatedLiveData

    fun addCard(cardNumber: String, cardName: String, expirationDate: String, securityCode: String,
                document: String) {
        var user = AppPreferences.getUser()!!
        if (user.cards == null) {
            user.cards = mutableListOf()
        }
        user.cards!!.add(Card(cardNumber, cardName, expirationDate, securityCode, document))
        AppPreferences.setUser(user)

        _cardCreatedLiveData.postValue("")
    }
}