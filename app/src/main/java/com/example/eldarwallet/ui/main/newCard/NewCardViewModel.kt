package com.example.eldarwallet.ui.main.newCard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.objects.Card
import com.example.eldarwallet.data.local.room.CardEntity
import kotlinx.coroutines.launch

class NewCardViewModel(application: Application): AndroidViewModel(application) {
    private val _cardCreatedLiveData = MutableLiveData<Any>()
    val cardCreatedLiveData: LiveData<Any>
        get() = _cardCreatedLiveData

    fun addCard(cardNumber: String, cardName: String, expirationDate: String, securityCode: String,
                document: String) {
        viewModelScope.launch {
            var user = AppPreferences.getUser()!!
            val db = EldarWalletApplication().getDatabase(getApplication())
            db.getItemsDao().insertCard(CardEntity(0, cardNumber, cardName, expirationDate, securityCode, document, user.id!!))

            if (user.cards == null) {
                user.cards = mutableListOf()
            }
            user.cards!!.add(Card(cardNumber, cardName, expirationDate, securityCode, document))
            AppPreferences.setUser(user)
        }

        _cardCreatedLiveData.postValue("")
    }
}