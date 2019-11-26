package com.takeaway.kiantestwork.di

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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





}
