package com.wordpress.lonelytripblog.funwithflags.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB
import com.wordpress.lonelytripblog.funwithflags.data.db.Country
import java.util.concurrent.Executor
import javax.inject.Inject

class GameRepository @Inject constructor(
        private val db: CountriesDB,
        private val executor: Executor): GameRepo {
    private var result: MediatorLiveData<GameEntity> = MediatorLiveData()
    private var learntCountries: MutableList<Country>? = null
    private var learntCountry: MediatorLiveData<Country>? = null
    private lateinit var currentCountry: Country

    override fun getLiveDataForGame(): LiveData<GameEntity> {
        addSourceToResultFromDb()
        return result
    }

    override fun nextFlag() {
        addSourceToResultFromDb()
    }

    override fun saveCurrentFlagIntoLearntFlags() {
        val countryToInsert = Country(currentCountry.id, currentCountry.name,
                currentCountry.resourceId, currentCountry.description, 1)
        executor.execute { db.countryDao().updateCounty(countryToInsert) }
    }

    override fun getAmountOfLeftFlags() = db.countryDao().getCountForType(0)

    override fun getLearntFlag(): LiveData<Country> {
        learntCountry = MediatorLiveData()
        if (learntCountries == null || learntCountries!!.isEmpty()) {
            learntCountry!!.addSource(db.countryDao().getLearntCountries()) { countries ->
                learntCountries = countries.toMutableList()
                postValueToLearntCountry()
            }
        } else {
            postValueToLearntCountry()
        }
        return learntCountry!!
    }

    override fun nextLearntFlag() {
        postValueToLearntCountry()
    }

    override fun getAmountOfLearntFlags(): LiveData<Int> = db.countryDao().getCountForType(1)

    private fun addSourceToResultFromDb() {
        val countryLiveData = db.countryDao().getRandomCountryToLearn()
        result.addSource(countryLiveData) { country ->
            if (country == null) {
                result.postValue(null)
                return@addSource
            }
            currentCountry = country
            val gameEntityLiveData = Transformations.map(db.countryDao()
                    .getRandomCountriesOtherThanChosen(country.id)) { input ->
                val rightAnswerPosition = (Math.random() * 4).toInt()
                GameEntity(currentCountry.resourceId,
                if (rightAnswerPosition > input.size) {
                    input.plus(currentCountry.name)
                } else {
                    input.toMutableList().apply { add(rightAnswerPosition, currentCountry.name) }
                }
                        , rightAnswerPosition)
            }
            result.addSource(gameEntityLiveData) { gameEntity ->
                result.postValue(gameEntity)
                result.removeSource(gameEntityLiveData)
            }
            result.removeSource(countryLiveData)
        }
    }

    private fun postValueToLearntCountry() {
        if (learntCountries!!.isEmpty()) {
            learntCountry!!.postValue(null)
        } else {
            learntCountry!!.postValue(learntCountries!!.removeAt(0))
        }
    }

}