package com.example.eldarwallet.ui.main.newCard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.AppPreferences
import com.example.eldarwallet.data.local.objects.Card
import com.example.eldarwallet.data.local.room.CardEntity
import com.example.eldarwallet.utils.AESEncryption
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class NewCardViewModel(application: Application) : BaseViewModel(application) {
    private val _cardCreatedLiveData = MutableLiveData<Any>()
    val cardCreatedLiveData: LiveData<Any>
        get() = _cardCreatedLiveData

    fun addCard(
        cardNumber: String, cardName: String, expirationDate: String, securityCode: String,
        document: String
    ) {
        viewModelScope.launch {
            var user = AppPreferences.getUser()!!
            val db = EldarWalletApplication().getDatabase(getApplication())
            val encryptedCardNumber = AESEncryption.encrypt(cardNumber)
            val encryptedCardName = AESEncryption.encrypt(cardName)
            val encryptedExpirationDate = AESEncryption.encrypt(expirationDate)
            val encryptedSecurityCode = AESEncryption.encrypt(securityCode)
            val encryptedDocument = AESEncryption.encrypt(document)
            if (encryptedCardNumber.isNullOrEmpty() || encryptedCardName.isNullOrEmpty() ||
                encryptedExpirationDate.isNullOrEmpty() || encryptedSecurityCode.isNullOrEmpty() ||
                encryptedDocument.isNullOrEmpty()
            ) {
                onError.postValue(Throwable("Error encrypting card info"))
            } else {
                db.getItemsDao().insertCard(
                    CardEntity(
                        0, encryptedCardNumber, encryptedCardName, encryptedExpirationDate,
                        encryptedSecurityCode, encryptedDocument, user.id!!
                    )
                )

                if (user.cards == null) {
                    user.cards = mutableListOf()
                }
                user.cards!!.add(
                    Card(
                        encryptedCardNumber, encryptedCardName, encryptedExpirationDate,
                        encryptedSecurityCode, encryptedDocument
                    )
                )
                AppPreferences.setUser(user)
            }
        }

        _cardCreatedLiveData.postValue("")
    }
}