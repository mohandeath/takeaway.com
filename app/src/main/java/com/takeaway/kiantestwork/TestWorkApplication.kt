package com.takeaway.kiantestwork

import android.app.Activity
import android.app.Application
import com.takeaway.kiantestwork.di.AppComponent
import com.takeaway.kiantestwork.di.AppModule
import com.takeaway.kiantestwork.di.DaggerAppComponent


import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class TestWorkApplication : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector : DispatchingAndroidInjector<Activity>

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .build()

        appComponent.inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

}
