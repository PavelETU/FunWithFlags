package com.wordpress.lonelytripblog.funwithflags

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DataBaseTests {

    private lateinit var _db: CountriesDB
    private val db: CountriesDB
        get() = _db

    @Before
    fun createDB() {
        _db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), CountriesDB::class.java).build()
    }

    @After
    fun closeDB() {
        _db.close()
    }

    @Test
    fun verifyGetAllLearntFlags() {
        val countriesList = listOf(Country(1, "Russia", R.drawable.ru, "Bla bla"),
                Country(2, "Germany", R.drawable.germany, "Bla bla", 1))
        db.countryDao().insertCountries(countriesList)

        val returnedCountry = getValueOfLiveData(db.countryDao().getRandomCountryToLearn())
        assertEquals(returnedCountry, countriesList[0])
        val returnedLearntCountry = getValueOfLiveData(db.countryDao().getLearntCountries())
        assertThat(returnedLearntCountry.size, `is`(1))
        assertEquals(returnedLearntCountry[0], countriesList[1])
    }

    @Test
    fun verifyRandomCountriesReturnedProperly() {
        val countriesList = listOf(Country(1, "Russia", R.drawable.ru, "Bla bla"),
                Country(2, "Germany", R.drawable.germany, "Bla bla", 1),
                Country(3, "Mexico", R.drawable.mexico, "Bla bla", 0),
                Country(4, "Costa Rico", R.drawable.costarica, "Bla bla", 1)
        )
        db.countryDao().insertCountries(countriesList)

        val countries = getValueOfLiveData(db.countryDao().getRandomCountriesOtherThanChosen(countriesList[3].id))
        assertThat(countries.size, `is`(3))
        assertTrue(countries.containsAll(arrayListOf(countriesList[0].name, countriesList[1].name,
                countriesList[2].name)))
    }

    @Test
    fun verifyAmountsOfRecords() {
        val countriesList = listOf(Country(1, "Russia", R.drawable.ru, "Bla bla"),
                Country(2, "Germany", R.drawable.germany, "Bla bla", 1),
                Country(3, "Mexico", R.drawable.mexico, "Bla bla", 0),
                Country(4, "Costa Rico", R.drawable.costarica, "Bla bla", 1),
                Country(5, "Thailand", R.drawable.thailand, "Bla bla", 1),
                Country(6, "Panama", R.drawable.panama, "Bla bla"),
                Country(7, "South Africa", R.drawable.southafrica, "Bla bla", 1)
        )
        db.countryDao().insertCountries(countriesList)

        var countriesCount = getValueOfLiveData(db.countryDao().getCountForType(0))
        assertThat(countriesCount, `is`(3))
        countriesCount = getValueOfLiveData(db.countryDao().getCountForType(1))
        assertThat(countriesCount, `is`(4))
    }

    private fun <T> getValueOfLiveData(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return data[0] as T
    }


}