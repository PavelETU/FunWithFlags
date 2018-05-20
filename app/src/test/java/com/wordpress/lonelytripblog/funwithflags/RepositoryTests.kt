package com.wordpress.lonelytripblog.funwithflags

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.data.GameRepository
import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import com.wordpress.lonelytripblog.funwithflags.data.db.CountryDao
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class RepositoryTests {

    private lateinit var repository: GameRepository
    private val db = mock(CountriesDB::class.java)
    private val dao = mock(CountryDao::class.java)

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        `when`(db.countryDao()).thenReturn(dao)
        repository = GameRepository(db)
    }

    @Test
    fun verifyRequestIsMadeProperly() {
        val liveDataCountry = MutableLiveData<Country>()
        val liveDataCountriesNames = MutableLiveData<List<String>>()
        val countries = arrayListOf("Some random country", "Another one", "And another one")
        val rightCountry = Country(5, "Name of Country", 235, "bla bla")

        liveDataCountry.postValue(rightCountry)
        liveDataCountriesNames.postValue(countries)
        `when`(dao.getRandomCountryToLearn()).thenReturn(liveDataCountry)
        `when`(dao.getRandomCountriesOtherThanChosen(ArgumentMatchers.anyInt())).thenReturn(liveDataCountriesNames)

        val observer = (mock(Observer::class.java) as Observer<GameEntity>)
        repository.liveDataForGame.observeForever(observer)

        verify(dao).getRandomCountryToLearn()
        verify(dao).getRandomCountriesOtherThanChosen(rightCountry.id)
        val argumentCaptor = ArgumentCaptor.forClass(GameEntity::class.java)
        verify(observer).onChanged(argumentCaptor.capture())
        val currentGameEntity = argumentCaptor.value
        assertNotNull(currentGameEntity)
        assertThat(currentGameEntity!!.countries.size, `is`(4))
        assertTrue(currentGameEntity.countries.containsAll(countries))
        assertTrue(currentGameEntity.countries.contains(rightCountry.name))
        assertThat(currentGameEntity.countries[currentGameEntity.rightAnswer], `is`(rightCountry.name))
    }

}