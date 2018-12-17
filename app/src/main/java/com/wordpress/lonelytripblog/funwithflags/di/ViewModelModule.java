package com.wordpress.lonelytripblog.funwithflags.di;

import android.content.Context;

import com.wordpress.lonelytripblog.funwithflags.data.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.data.GameRepository;
import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB;
import com.wordpress.lonelytripblog.funwithflags.util.Counter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    private Context appContext;

    public ViewModelModule(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @NonNull
    @Singleton
    GameRepo gameRepo(CountriesDB db, Executor backgroundExecutor) {
        return new GameRepository(db, backgroundExecutor);
    }

    @Provides
    @NonNull
    @Singleton
    Executor getBackgroundExecutor() {
        return Executors.newSingleThreadExecutor();
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

}
