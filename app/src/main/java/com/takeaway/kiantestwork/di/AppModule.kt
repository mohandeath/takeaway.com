package com.takeaway.kiantestwork.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.takeaway.kiantestwork.DATABASE_NAME
import com.takeaway.kiantestwork.data.datasources.local.Database
import com.takeaway.kiantestwork.data.datasources.local.RestaurantDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * This module provides all the app level and generic dependencies, like app context, networking, db, etc.
 */
@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    internal fun providesApplication(): Application {
        return application
    }


    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideRestaurantsDatabase(): Database {
        return Room.databaseBuilder(
            application,
            Database::class.java, DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRestaurantsDao(database: Database): RestaurantDAO = database.restaurantDAO()






}
