package com.wordpress.lonelytripblog.funwithflags.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import com.wordpress.lonelytripblog.funwithflags.data.db.CountriesDB;
import com.wordpress.lonelytripblog.funwithflags.data.db.Country;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

/**
 * Created by Павел on 09.03.2018.
 */

@Singleton
public class GameRepository implements GameRepo {

    private CountriesDB db;
    private MediatorLiveData<GameEntity> result;
    private List<Country> learntCountries;
    private MutableLiveData<Country> learntCountry;
    private Executor executor;

    private Country currentCountry;

    public GameRepository(CountriesDB db, Executor executor) {
        this.db = db;
        this.executor = executor;
    }

    @Override
    public LiveData<GameEntity> getLiveDataForGame() {
        result = new MediatorLiveData<>();
        addSourceToResultFromDb();
        return result;
    }

    @Override
    public void nextFlag() {
        addSourceToResultFromDb();
    }

    private void addSourceToResultFromDb() {
        LiveData<Country> countryLiveData = db.countryDao().getRandomCountryToLearn();
        result.addSource(countryLiveData, country -> {
            if (country == null) {
                result.postValue(null);
                return;
            }
            currentCountry = country;
            LiveData<GameEntity> gameEntityLiveData = Transformations.map(db.countryDao()
                                    .getRandomCountriesOtherThanChosen(country.getId()), input -> {
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
        Country countryToInsert = new Country(currentCountry.getId(), currentCountry.getName(),
                currentCountry.getResourceId(), currentCountry.getDescription(), 1);
        executor.execute(() -> db.countryDao().updateCounty(countryToInsert));
    }

    @Override
    public LiveData<Integer> getAmountOfLeftFlags() {
        return db.countryDao().getCountForType(0);
    }

    @Override
    public LiveData<Country> getLearntFlag() {
        learntCountry = new MutableLiveData<>();
        if (learntCountries == null) {
            MediatorLiveData<List<Country>> mediatorLiveData = new MediatorLiveData<>();
            mediatorLiveData.addSource(db.countryDao().getLearntCountries(), countries -> {
                learntCountries = countries;
                postValueToLearntCountry();
            });
        } else {
            postValueToLearntCountry();
        }
        return learntCountry;
    }

    private void postValueToLearntCountry() {
        if (learntCountries.isEmpty()) {
            learntCountry.postValue(null);
        } else {
            learntCountry.postValue(learntCountries.remove(0));
        }
    }

    @Override
    public void nextLearntFlag() {
        postValueToLearntCountry();
    }

    @Override
    public LiveData<Integer> getAmountOfLearntFlags() {
        return db.countryDao().getCountForType(1);
    }
}
