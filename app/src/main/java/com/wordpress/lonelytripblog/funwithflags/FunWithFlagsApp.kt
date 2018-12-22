package com.wordpress.lonelytripblog.funwithflags

import android.app.Activity
import android.app.Application
import com.wordpress.lonelytripblog.funwithflags.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class FunWithFlagsApp : Application(), HasActivityInjector {
    override fun onCreate() {
        super.onCreate()
        AppInjector.inject(this)
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector


}