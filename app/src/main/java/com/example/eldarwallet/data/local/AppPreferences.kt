package com.example.eldarwallet.data.local

import android.content.Context
import com.example.eldarwallet.EldarWalletApplication
import com.example.eldarwallet.data.local.objects.User
import com.google.gson.Gson

object AppPreferences {
    private const val USER = "USER"
    private const val APP_PREFERENCES = "APP_PREFERENCES"

    private fun getPreferences() = EldarWalletApplication.instance.getSharedPreferences(
        APP_PREFERENCES, Context.MODE_PRIVATE
    )

    fun getUser(): User? {
        val json = getPreferences().getString(USER, "")
        return Gson().fromJson(json, User::class.java)
    }

    fun setUser(user: User?) = getPreferences().edit()?.putString(USER, Gson().toJson(user))?.apply()

    fun removeUser(context: Context) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .remove(USER)
            .apply()
    }
}