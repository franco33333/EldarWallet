package com.example.eldarwallet.utils

import android.view.View
import com.example.eldarwallet.R

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun getCardLogo(cardNumber: String): Int? {
    val number = cardNumber.first().digitToIntOrNull()
    if (number!=null) {
        return when(number) {
            3 -> { R.drawable.amex_logo }
            4 -> { R.drawable.visa_logo }
            5 -> { R.drawable.mastercard_logo }
            else -> { null }
        }
    }
    return null
}
