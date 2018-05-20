package com.wordpress.lonelytripblog.funwithflags.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;

import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB;
import com.wordpress.lonelytripblog.funwithflags.data.db.Country;

import javax.inject.Singleton;

/**
 * Created by Павел on 09.03.2018.
 */

@Singleton
public class GameRepository implements GameRepo {

    private CountriesDB db;
    private MediatorLiveData<GameEntity> result;

    private Country currentCountry;

    public GameRepository(CountriesDB db) {
        this.db = db;
    }

    @Override
    public LiveData<GameEntity> getLiveDataForGame() {
        result = new MediatorLiveData<>();
        LiveData<Country> countryLiveData = db.countryDao().getRandomCountryToLearn();
        result.addSource(countryLiveData, country -> {
            if (country == null) {
                result.postValue(null);
                return;
            }
            currentCountry = country;
            LiveData<GameEntity> gameEntityLiveData =
                    Transformations.map(db.countryDao().getRandomCountriesOtherThanChosen(
                            country.getId()), input -> {
                        int rightAnswerPosition = (int) (Math.random() * 4);
                        if (rightAnswerPosition > input.size()) {
                            input.add(currentCountry.getName());
                        } else {
                            input.add(rightAnswerPosition, currentCountry.getName());
                        }
                        return new GameEntity(currentCountry.getResourceId(), input, rightAnswerPosition);
                    });
            result.addSource(gameEntityLiveData, gameEntity -> {
                result.postValue(gameEntity);
                result.removeSource(gameEntityLiveData);
            });
            result.removeSource(countryLiveData);
        });
        return result;
    }

    @Override
    public void nextFlag() {
        LiveData<Country> countryLiveData = db.countryDao().getRandomCountryToLearn();
        result.addSource(countryLiveData, country -> {
            if (country == null) {
                result.postValue(null);
                return;
            }
            currentCountry = country;
            LiveData<GameEntity> gameEntityLiveData =
                    Transformations.map(db.countryDao().getRandomCountriesOtherThanChosen(
                            country.getId()), input -> {
                        int rightAnswerPosition = (int) (Math.random() * 4);
                        if (rightAnswerPosition > input.size()) {
                            input.add(currentCountry.getName());
                        } else {
                            input.add(rightAnswerPosition, currentCountry.getName());
                        }
                        return new GameEntity(currentCountry.getResourceId(), input, rightAnswerPosition);
                    });
            result.addSource(gameEntityLiveData, gameEntity -> {
                result.postValue(gameEntity);
                result.removeSource(gameEntityLiveData);
            });
            result.removeSource(countryLiveData);
        });
    }

    @Override
    public void saveCurrentFlagIntoLearntFlags() {

    }

    @Override
    public LiveData<Integer> getAmountOfLeftFlags() {
        return null;
    }

    @Override
    public LiveData<GameEntity> getLearntFlag() {
        return null;
    }

    @Override
    public void nextLearntFlag() {

    }

    @Override
    public LiveData<Integer> getAmountOfLearntFlags() {
        return null;
    }
}
