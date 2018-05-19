package com.wordpress.lonelytripblog.funwithflags.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.wordpress.lonelytripblog.funwithflags.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Singleton;

/**
 * Created by Павел on 09.03.2018.
 */

@Singleton
public class GameRepository implements GameRepo {

    private MutableLiveData<GameEntity> result;
    private List<String> countries = new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand",
            "Germany", "Canada", "Bangladesh", "Costa Rica", "Mexico", "Morocco", "Norway",
            "Panama", "South Africa", "Poland", "Latvia", "Estonia", "Argentina", "Bolivia"));
    private List<GameEntity> gameEntities = new ArrayList<>(Arrays.asList(
            new GameEntity(R.drawable.germany,
                    new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3),
            new GameEntity(R.drawable.canada,
                    new ArrayList<>(Arrays.asList("Canada", "Estonia", "Thailand", "Morocco")), 0),
            new GameEntity(R.drawable.bangladesh,
                    new ArrayList<>(Arrays.asList("Germany", "Bangladesh", "Canada", "Morocco")), 1),
            new GameEntity(R.drawable.costarica,
                    new ArrayList<>(Arrays.asList("Germany", "Bangladesh", "Costa Rica", "Morocco")), 2),
            new GameEntity(R.drawable.mexico,
                    new ArrayList<>(Arrays.asList("Germany", "Bangladesh", "Costa Rica", "Mexico")), 3),
            new GameEntity(R.drawable.morocco,
                    new ArrayList<>(Arrays.asList("Germany", "Morocco", "Costa Rica", "Mexico")), 1),
            new GameEntity(R.drawable.norway,
                    new ArrayList<>(Arrays.asList("Norway", "Morocco", "Costa Rica", "Mexico")), 0),
            new GameEntity(R.drawable.panama,
                    new ArrayList<>(Arrays.asList("Norway", "Morocco", "Panama", "Mexico")), 2),
            new GameEntity(R.drawable.ru,
                    new ArrayList<>(Arrays.asList("Norway", "Morocco", "Panama", "Russia")), 3),
            new GameEntity(R.drawable.southafrica,
                    new ArrayList<>(Arrays.asList("Norway", "Morocco", "Panama", "South Africa")), 3),
            new GameEntity(R.drawable.thailand,
                    new ArrayList<>(Arrays.asList("Thailand", "Morocco", "Panama", "South Africa")), 0),
            new GameEntity(R.drawable.us,
                    new ArrayList<>(Arrays.asList("Thailand", "US", "Panama", "South Africa")), 1)

    ));

    public GameRepository() {
        result = new MutableLiveData<>();
        result.setValue(gameEntities.remove((int)(Math.random() * gameEntities.size())));
    }

    @Override
    public LiveData<GameEntity> getLiveDataForGame() {
        return result;
    }

    @Override
    public void nextFlag() {
        if (gameEntities.size() == 0) {
            gameEntities = new ArrayList<>(Arrays.asList(
                    new GameEntity(R.drawable.germany,
                            new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3),
                    new GameEntity(R.drawable.canada,
                            new ArrayList<>(Arrays.asList("Canada", "Estonia", "Thailand", "Morocco")), 0),
                    new GameEntity(R.drawable.bangladesh,
                            new ArrayList<>(Arrays.asList("Germany", "Bangladesh", "Canada", "Morocco")), 1),
                    new GameEntity(R.drawable.costarica,
                            new ArrayList<>(Arrays.asList("Germany", "Bangladesh", "Costa Rica", "Morocco")), 2),
                    new GameEntity(R.drawable.mexico,
                            new ArrayList<>(Arrays.asList("Germany", "Bangladesh", "Costa Rica", "Mexico")), 3),
                    new GameEntity(R.drawable.morocco,
                            new ArrayList<>(Arrays.asList("Germany", "Morocco", "Costa Rica", "Mexico")), 1),
                    new GameEntity(R.drawable.norway,
                            new ArrayList<>(Arrays.asList("Norway", "Morocco", "Costa Rica", "Mexico")), 0),
                    new GameEntity(R.drawable.panama,
                            new ArrayList<>(Arrays.asList("Norway", "Morocco", "Panama", "Mexico")), 2),
                    new GameEntity(R.drawable.ru,
                            new ArrayList<>(Arrays.asList("Norway", "Morocco", "Panama", "Russia")), 3),
                    new GameEntity(R.drawable.southafrica,
                            new ArrayList<>(Arrays.asList("Norway", "Morocco", "Panama", "South Africa")), 3),
                    new GameEntity(R.drawable.thailand,
                            new ArrayList<>(Arrays.asList("Thailand", "Morocco", "Panama", "South Africa")), 0),
                    new GameEntity(R.drawable.thailand,
                            new ArrayList<>(Arrays.asList("Thailand", "US", "Panama", "South Africa")), 1)

            ));
        }
        result.setValue(gameEntities.remove((int)(Math.random() * gameEntities.size())));
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
