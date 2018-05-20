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
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(3, "Mexico", R.drawable.mexico, "Capital: Mexico City\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(4, "Costa Rica", R.drawable.costarica, "Capital: San Jose\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(5, "Thailand", R.drawable.thailand, "Capital: Bangkok\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(6, "Panama", R.drawable.panama, "Capital: Panama City\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
                Country(7, "South Africa", R.drawable.southafrica,
                        "Capital: Pretoria (executive), Bloemfontein (judicial), " +
                                "Cape Town (legislative)\n" +
                                "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                                "Area total: 17,098,246km^2"),
                Country(8, "Canada", R.drawable.canada, "Capital: Ottawa\n" +
                        "Official languages: Russian\nPopulation: 144,526,636 (2018)\n" +
                        "Area total: 17,098,246km^2"),
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