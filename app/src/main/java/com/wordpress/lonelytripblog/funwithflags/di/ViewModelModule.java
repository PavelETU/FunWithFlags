package com.wordpress.lonelytripblog.funwithflags.di;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wordpress.lonelytripblog.funwithflags.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.GameRepository;
import com.wordpress.lonelytripblog.funwithflags.GameViewModel;
import com.wordpress.lonelytripblog.funwithflags.GameViewModel.GameViewModelFactory;

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
    static GameRepo gameRepo() {
        return new GameRepository();
    }

    @Provides
    @NonNull
    static ViewModelProvider.Factory provideViewModelFactory(GameRepo gameRepo) {
        return new GameViewModelFactory(gameRepo);
    }

}
