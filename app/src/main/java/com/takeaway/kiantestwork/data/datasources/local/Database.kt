package com.takeaway.kiantestwork.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.takeaway.kiantestwork.data.dto.Restaurant

@Database(entities = [Restaurant::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun restaurantDAO(): RestaurantDAO
}