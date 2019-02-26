package com.wordpress.lonelytripblog.funwithflags.di

import com.wordpress.lonelytripblog.funwithflags.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsContributor {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeGameFragment(): GameFragment

    @ContributesAndroidInjector
    abstract fun contributeRecapFragment(): RecapFragment

    @ContributesAndroidInjector
    abstract fun contributeAboutFragment(): AboutFragment
}