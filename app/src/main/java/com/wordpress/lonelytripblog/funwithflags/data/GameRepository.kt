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
        private val executor: Executor) : GameRepo {
    private var unknownFlagGameEntity: MediatorLiveData<GameEntity> = MediatorLiveData()
    private var learntCountries: MutableList<Country> = mutableListOf()
    private var learntCountry: MediatorLiveData<Country> = MediatorLiveData()
    private lateinit var currentCountry: Country

    override fun getUnknownCountryGameEntity(): LiveData<GameEntity> {
        updateGameEntityWithNextValueOrNullIfAllFlagsAreLearnt()
        return unknownFlagGameEntity
    }

    override fun requestNewGameEntity() {
        updateGameEntityWithNextValueOrNullIfAllFlagsAreLearnt()
    }

    override fun saveCurrentFlagIntoLearntFlags() {
        val countryToInsert = Country(currentCountry.id, currentCountry.name,
                currentCountry.resourceId, currentCountry.description, 1)
        executor.execute { db.countryDao().updateCounty(countryToInsert) }
    }

    override fun getAmountOfLeftFlags() = db.countryDao().getCountForType(0)

    override fun getLearntFlags(): LiveData<List<Country>> {
        return db.countryDao().getLearntCountries()
    }

    override fun getAmountOfLearntFlags(): LiveData<Int> = db.countryDao().getCountForType(1)

    private fun updateGameEntityWithNextValueOrNullIfAllFlagsAreLearnt() {
        val countryLiveData = db.countryDao().getRandomCountryToLearn()
        unknownFlagGameEntity.addSource(countryLiveData) { country ->
            if (db.dbWasCreated.value == true) {
                if (country == null) {
                    unknownFlagGameEntity.postValue(null)
                    return@addSource
                }
                currentCountry = country
                val gameEntityLiveData = generateGameEntityWithCurrentCountryAsAnswer()
                unknownFlagGameEntity.addSource(gameEntityLiveData) { gameEntity ->
                    unknownFlagGameEntity.postValue(gameEntity)
                    unknownFlagGameEntity.removeSource(gameEntityLiveData)
                }
                unknownFlagGameEntity.removeSource(countryLiveData)
            }
        }
    }

    private fun generateGameEntityWithCurrentCountryAsAnswer(): LiveData<GameEntity> =
            Transformations.map(db.countryDao()
                    .getRandomCountriesOtherThanChosen(currentCountry.id)) { randomCountries ->
                getGameEntityWithCurrentCountry(randomCountries)
            }

    private fun getGameEntityWithCurrentCountry(randomCountries: List<String>): GameEntity {
        val rightAnswerPosition = (Math.random() * 4).toInt()
        return GameEntity(currentCountry.resourceId,
                if (rightAnswerPosition > randomCountries.size) {
                    randomCountries.plus(currentCountry.name)
                } else {
                    randomCountries.toMutableList().apply { add(rightAnswerPosition, currentCountry.name) }
                }
                , rightAnswerPosition)
    }


    private fun postValueToLearntCountry() {
        if (learntCountries.isEmpty()) {
            learntCountry.postValue(null)
        } else {
            learntCountry.postValue(learntCountries.removeAt(0))
        }
    }

}