package com.wordpress.lonelytripblog.funwithflags.di;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.wordpress.lonelytripblog.funwithflags.data.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.data.GameRepository;
import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB;
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

    private Context appContext;

    public ViewModelModule(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @NonNull
    @Singleton
    GameRepo gameRepo(CountriesDB db) {
        return new GameRepository(db);
    }

    @Provides
    @NonNull
    @Singleton
    CountriesDB getCountriesDB(Context appContext) {
        return CountriesDB.Companion.getInstance(appContext);
    }

    @Provides
    @NonNull
    @Singleton
    Context getAppContext() {
        return appContext;
    }

    @Provides
    @NonNull
    @Singleton
    Counter getCounter() {
        return new Counter();
    }

    @Provides
    @NonNull
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(GameRepo gameRepo, Counter counter) {
        return new GameViewModelFactory(gameRepo, counter);
    }

}
