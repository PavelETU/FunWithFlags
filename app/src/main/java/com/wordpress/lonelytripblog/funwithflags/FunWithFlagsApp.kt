package com.wordpress.lonelytripblog.funwithflags

import android.app.Application
import com.wordpress.lonelytripblog.funwithflags.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class FunWithFlagsApp : Application(), HasAndroidInjector {
    override fun onCreate() {
        super.onCreate()
        AppInjector.inject(this)
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}