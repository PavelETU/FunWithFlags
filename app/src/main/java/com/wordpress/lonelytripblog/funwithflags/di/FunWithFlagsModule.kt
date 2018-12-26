package com.wordpress.lonelytripblog.funwithflags.di

import android.content.Context
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.data.GameRepository
import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import com.wordpress.lonelytripblog.funwithflags.util.CounterImpl
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class FunWithFlagsModule(private val appContext: Context) {

    @Provides
    @Singleton
    fun provideGameRepo(db: CountriesDB, executor: Executor): GameRepo = GameRepository(db, executor)

    @Provides
    @Singleton
    fun provideBackgroundExecutor(): Executor = Executors.newSingleThreadExecutor()

    @Provides
    @Singleton
    fun provideCountriesDB(): CountriesDB = CountriesDB.getInstance(appContext)

    @Provides
    @Singleton
    fun provideCounter(): Counter = CounterImpl()

}