package com.example.eldarwallet

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.eldarwallet.data.local.room.ItemsDatabase

class EldarWalletApplication: Application() {

    private lateinit var databaseInstance: ItemsDatabase

    companion object {
        lateinit var instance: EldarWalletApplication
        lateinit var appContext: Context
    }

    fun getDatabase(context: Context): ItemsDatabase {
        synchronized(ItemsDatabase::class.java) {
            if (!::databaseInstance.isInitialized) {
                databaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemsDatabase::class.java,
                    "application_db"
                )
                .fallbackToDestructiveMigration()
                .build()
            }
        }
        return databaseInstance
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}