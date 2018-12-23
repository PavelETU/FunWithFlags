package com.wordpress.lonelytripblog.funwithflags.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import com.wordpress.lonelytripblog.funwithflags.viewmodels.InfoViewModel
import com.wordpress.lonelytripblog.funwithflags.viewmodels.RecapViewModel
import com.wordpress.lonelytripblog.funwithflags.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsProvider {
    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindGameViewModel(viewModel: GameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoViewModel::class)
    abstract fun bindInfoViewModel(viewModel: InfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecapViewModel::class)
    abstract fun bindRecapViewModel(viewModel: RecapViewModel): ViewModel

    @Binds
    abstract fun bindFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}