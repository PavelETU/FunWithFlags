package com.wordpress.lonelytripblog.funwithflags;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wordpress.lonelytripblog.funwithflags.data.GameEntity;
import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Павел on 20.03.2018.
 */

@RunWith(AndroidJUnit4.class)
public class UITests {

    @Rule
    public ActivityTestRule<ActivityToTestFragments> activityTestRule =
            new ActivityTestRule<>(ActivityToTestFragments.class, true, true);

    private MutableLiveData<GameEntity> fakeGameEntity = new MutableLiveData<>();
    private GameViewModel fakeViewModel;

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
        activityTestRule.getActivity().setFragment(gameFragment);
    }

    @Test
    public void verifyThatTitlesCorrespondsToData() {
        List<String> countriesNames = new ArrayList<>();
        countriesNames.add("China");
        countriesNames.add("Netherlands");
        countriesNames.add("Poland");
        countriesNames.add("Dominican Republic");
        fakeGameEntity.postValue(new GameEntity(R.drawable.germany, countriesNames, 2));
        onView(withId(R.id.button)).check(matches(withText(countriesNames.get(0))));
        onView(withId(R.id.button2)).check(matches(withText(countriesNames.get(1))));
        onView(withId(R.id.button3)).check(matches(withText(countriesNames.get(2))));
        onView(withId(R.id.button4)).check(matches(withText(countriesNames.get(3))));
    }

    @Test
    public void verifyThatTitlesIsChangedAfterDataIsChanged() {
        List<String> countriesNames = new ArrayList<>();
        countriesNames.add("China");
        countriesNames.add("Netherlands");
        countriesNames.add("Poland");
        countriesNames.add("Dominican Republic");
        fakeGameEntity.postValue(new GameEntity(R.drawable.us, countriesNames, 2));
        countriesNames.clear();
        countriesNames.add("Ireland");
        countriesNames.add("Cyprus");
        countriesNames.add("Mexico");
        countriesNames.add("Cuba");
        fakeGameEntity.postValue(new GameEntity(R.drawable.thailand, countriesNames, 2));
        onView(withId(R.id.button)).check(matches(withText(countriesNames.get(0))));
        onView(withId(R.id.button2)).check(matches(withText(countriesNames.get(1))));
        onView(withId(R.id.button3)).check(matches(withText(countriesNames.get(2))));
        onView(withId(R.id.button4)).check(matches(withText(countriesNames.get(3))));
    }

}
