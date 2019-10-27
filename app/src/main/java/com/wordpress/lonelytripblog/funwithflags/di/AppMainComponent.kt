package com.wordpress.lonelytripblog.funwithflags.di

import com.wordpress.lonelytripblog.funwithflags.FunWithFlagsApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [FunWithFlagsModule::class, ActivityScopeModule::class,
    AndroidInjectionModule::class,
    ActivityContributor::class,
    ViewModelsProvider::class])
interface AppMainComponent {
    fun injectApp(app: FunWithFlagsApp)
}