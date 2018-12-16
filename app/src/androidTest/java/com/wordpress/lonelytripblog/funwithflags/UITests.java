package com.wordpress.lonelytripblog.funwithflags;

import com.wordpress.lonelytripblog.funwithflags.data.GameEntity;
import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment;
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UITests {

    private MutableLiveData<GameEntity> fakeGameEntity = new MutableLiveData<>();
    private GameViewModel fakeViewModel;
    private NavigationController navigationController;

    @Before
    public void init() {
        final GameFragment gameFragment = new GameFragment();
        fakeViewModel = mock(GameViewModel.class);
        when(fakeViewModel.getGameEntity()).thenReturn(fakeGameEntity);
        gameFragment.viewModelFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public GameViewModel create(@NonNull Class modelClass) {
                return fakeViewModel;
            }
        };
        navigationController = mock(NavigationController.class);
        gameFragment.navigationController = navigationController;
        ActivityScenario<ActivityToTestFragments> activityScenario = ActivityScenario.launch(ActivityToTestFragments.class);
        activityScenario.onActivity(activity -> activity.setFragment(gameFragment));
    }

    @Test
    public void verifyThatDataCorrectlySet() {
        List<String> countriesNames = new ArrayList<>();
        countriesNames.add("China");
        countriesNames.add("Netherlands");
        countriesNames.add("Poland");
        countriesNames.add("Dominican Republic");
        fakeGameEntity.postValue(new GameEntity(R.drawable.germany, countriesNames, 0));
        // TODO refactor to countDownLatch
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(fakeViewModel).setRightAnswer(0);
        onView(withId(R.id.button)).check(matches(withText(countriesNames.get(0))));
        onView(withId(R.id.button2)).check(matches(withText(countriesNames.get(1))));
        onView(withId(R.id.button3)).check(matches(withText(countriesNames.get(2))));
        onView(withId(R.id.button4)).check(matches(withText(countriesNames.get(3))));
    }

    @Test
    public void verifyThatDataCorrectlySetChanges() {
        List<String> countriesNames = new ArrayList<>();
        countriesNames.add("China");
        countriesNames.add("Netherlands");
        countriesNames.add("Poland");
        countriesNames.add("Dominican Republic");
        fakeGameEntity.postValue(new GameEntity(R.drawable.us, countriesNames, 2));
        // TODO refactor to countDownLatch
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(fakeViewModel).setRightAnswer(2);
        countriesNames.clear();
        countriesNames.add("Ireland");
        countriesNames.add("Cyprus");
        countriesNames.add("Mexico");
        countriesNames.add("Cuba");
        fakeGameEntity.postValue(new GameEntity(R.drawable.thailand, countriesNames, 3));
        // TODO refactor to countDownLatch
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(fakeViewModel).setRightAnswer(3);
        onView(withId(R.id.button)).check(matches(withText(countriesNames.get(0))));
        onView(withId(R.id.button2)).check(matches(withText(countriesNames.get(1))));
        onView(withId(R.id.button3)).check(matches(withText(countriesNames.get(2))));
        onView(withId(R.id.button4)).check(matches(withText(countriesNames.get(3))));
    }

}
