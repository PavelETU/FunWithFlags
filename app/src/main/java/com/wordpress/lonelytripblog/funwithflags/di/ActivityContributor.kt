package com.wordpress.lonelytripblog.funwithflags.di

import com.wordpress.lonelytripblog.funwithflags.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributor {
    @ContributesAndroidInjector(modules = [FragmentsContributor::class])
    abstract fun mainActivity(): MainActivity
}