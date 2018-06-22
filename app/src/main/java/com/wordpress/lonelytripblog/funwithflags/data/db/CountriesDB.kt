package com.wordpress.lonelytripblog.funwithflags.data.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.wordpress.lonelytripblog.funwithflags.R
import java.util.concurrent.Executors

@Database(entities = [(Country::class)], version = 1)
abstract class CountriesDB : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    // Create database by singleton pattern with use of volatile
    companion object {
        @Volatile
        private var INSTANCE: CountriesDB? = null

        fun getInstance(context: Context): CountriesDB =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(appContext: Context) = Room
                .databaseBuilder(appContext, CountriesDB::class.java, "countries.db")
                // Add callback to prepopulate database when database created on device
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute({
                            getInstance(appContext).countryDao().insertCountries(countriesList)
                        })
                    }
                }).build()

        private val countriesList = listOf(
                Country(1, "Russia", R.drawable.ru, "Capital: Moscow\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(2, "Germany", R.drawable.germany, "Capital: Berlin\n" +
                        "Official languages: German\nPopulation: 82,200,000 (2017)\n" +
                        "Area total: 357,168km^2"),
                Country(3, "Mexico", R.drawable.mexico, "Capital: Mexico City\n" +
                        "Official languages: None. National language: Spanish\nPopulation: 123,675,325 (2017)\n" +
                        "Area total: 1,972,550km^2"),
                Country(4, "Costa Rica", R.drawable.costarica, "Capital: San Jose\n" +
                        "Official languages: Spanish\nPopulation: 4,857,274 (2016)\n" +
                        "Area total: 51,100km^2"),
                Country(5, "Thailand", R.drawable.thailand, "Capital: Bangkok\n" +
                        "Official languages: Thai\nPopulation: 68,863,514 (2016)\n" +
                        "Area total: 513,120km^2"),
                Country(6, "Panama", R.drawable.panama, "Capital: Panama City\n" +
                        "Official languages: Spanish\nPopulation: 4,034,119 (2016)\n" +
                        "Area total: 75,417km^2"),
                Country(7, "South Africa", R.drawable.southafrica,
                        "Capital: Pretoria (executive), Bloemfontein (judicial), " +
                                "Cape Town (legislative)\n" +
                                "Official languages: 11 languages (Zulu, Xhosa, Afrikaans, English...)\n" +
                                "Population: 55,653,654 (2016)\n" +
                                "Area total: 1,221,037km^2"),
                Country(8, "Canada", R.drawable.canada, "Capital: Ottawa\n" +
                        "Official languages: English, French\nPopulation: 37,067,011 (2018)\n" +
                        "Area total: 9,984,670km^2"),
                Country(9, "Bangladesh", R.drawable.bangladesh, "Capital: Dhaka\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(10, "Morocco", R.drawable.morocco, "Capital: Rabat\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(11, "Norway", R.drawable.norway, "Capital: Oslo\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(12, "United States of America", R.drawable.us,
                        "Capital: Washington D. C.\n" +
                                "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                                "Area total: 17,098,246km^2")
        )
    }

}