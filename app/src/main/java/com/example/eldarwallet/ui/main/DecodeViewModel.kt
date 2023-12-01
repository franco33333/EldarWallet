package com.example.eldarwallet.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.data.local.objects.Card
import com.example.eldarwallet.utils.AESEncryption
import com.example.eldarwallet.utils.BaseViewModel
import kotlinx.coroutines.launch

class DecodeViewModel(application: Application): BaseViewModel(application) {

    private val _cardsDecryptedLiveData = MutableLiveData<MutableList<Card>>()
    val cardsDecryptedLiveData: LiveData<MutableList<Card>>
        get() = _cardsDecryptedLiveData

    fun decryptCardData(cards: MutableList<Card>) {
        viewModelScope.launch {
            val decryptedCards = mutableListOf<Card>()
            cards.forEach {
                if (it.number.isNullOrEmpty() || it.name.isNullOrEmpty() || it.expirationDate.isNullOrEmpty() ||
                    it.securityCode.isNullOrEmpty() || it.document.isNullOrEmpty()
                ) {
                    onError.postValue(Throwable("Error decrypting card data"))
                } else {
                    val decryptCardNumber = AESEncryption.decrypt(it.number!!)
                    val decryptCardName = AESEncryption.decrypt(it.name!!)
                    val decryptExpirationDate = AESEncryption.decrypt(it.expirationDate!!)
                    val decryptSecurityCode = AESEncryption.decrypt(it.securityCode!!)
                    val decryptDocument = AESEncryption.decrypt(it.document!!)
                    val decryptedCard = Card(decryptCardNumber, decryptCardName, decryptExpirationDate,
                        decryptSecurityCode, decryptDocument)
                    decryptedCards.add(decryptedCard)
                }
            }
            _cardsDecryptedLiveData.postValue(decryptedCards)
        }
    }
}