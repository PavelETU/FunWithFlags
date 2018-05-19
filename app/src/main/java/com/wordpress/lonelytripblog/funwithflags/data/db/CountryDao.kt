package com.wordpress.lonelytripblog.funwithflags.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: List<Country>)

    @Update
    fun updateCounty(country: Country)

    @Query("SELECT * FROM country WHERE flagIsLearnt=0 ORDER BY RANDOM() LIMIT 1")
    fun getRandomCountryToLearn() : LiveData<Country>

    @Query("SELECT * FROM country WHERE flagIsLearnt=1 ORDER BY RANDOM() LIMIT 1")
    fun getLearntCountries() : LiveData<List<Country>>

    @Query("SELECT name FROM country WHERE id != :chosenCountryId ORDER BY RANDOM() LIMIT 3")
    fun getRandomCountriesOtherThanChosen(chosenCountryId: Int) : LiveData<List<String>>

}