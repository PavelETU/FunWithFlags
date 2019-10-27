package com.wordpress.lonelytripblog.funwithflags.di

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.wordpress.lonelytripblog.funwithflags.util.GameFragmentFactory
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import dagger.Module
import dagger.Provides

@Module
class ActivityScopeModule {
    @Provides
    @ActivityScope
    fun provideFragmentFactory(viewModelFactory: ViewModelProvider.Factory,
                               navigationController: NavigationController): FragmentFactory =
            GameFragmentFactory(viewModelFactory, navigationController)
}