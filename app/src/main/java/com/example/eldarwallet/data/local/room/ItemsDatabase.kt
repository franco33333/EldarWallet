package com.example.eldarwallet.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, CardEntity::class],
    version = 1
)
abstract class ItemsDatabase: RoomDatabase() {
    abstract fun getItemsDao(): ApplicationDao
}