package com.wordpress.lonelytripblog.funwithflags.di;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wordpress.lonelytripblog.funwithflags.data.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.data.GameRepository;
import com.wordpress.lonelytripblog.funwithflags.util.Counter;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel.GameViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Павел on 09.03.2018.
 */

@Module
public class ViewModelModule {

    @Provides
    @NonNull
    @Singleton
    static GameRepo gameRepo() {
        return new GameRepository();
    }

    @Provides
    @NonNull
    @Singleton
    static Counter getCounter() {
        return new Counter();
    }

    @Provides
    @NonNull
    @Singleton
    static ViewModelProvider.Factory provideViewModelFactory(GameRepo gameRepo, Counter counter) {
        return new GameViewModelFactory(gameRepo, counter);
    }

}
