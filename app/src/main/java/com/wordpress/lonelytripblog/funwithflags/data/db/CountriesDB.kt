package com.wordpress.lonelytripblog.funwithflags.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(Country::class)], version = 1)
abstract class CountriesDB : RoomDatabase() {
    abstract fun countryDao() : CountryDao
}