package com.wordpress.lonelytripblog.funwithflags;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.widget.Button;

import com.wordpress.lonelytripblog.funwithflags.data.GameEntity;
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Павел on 04.03.2018.
 */
@RunWith(JUnit4.class)
public class GameViewModelTest {

    @Rule
    public InstantTaskExecutorRule ruleForTestingArchitectureComponents = new InstantTaskExecutorRule();
    @Mock
    GameRepo gameRepo;
    GameViewModel viewModel;
    GameEntity currentGameEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        currentGameEntity = new GameEntity("http://www.countryflags.io/de/shiny/64.png",
                new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3);
        viewModel = new GameViewModel(gameRepo);
        MutableLiveData<GameEntity> result = new MutableLiveData<>();
        result.setValue(currentGameEntity);
        when(gameRepo.getNewFlag()).then(invocation -> result);
    }

    @Test
    public void showRightAnswerAfterAnythingChosen() throws Exception {
        // Choose
        int currentAnswer = 0;
        viewModel.getAnswerByUser(currentAnswer);
        assertTrue("After first click item should be animated",
                viewModel.animateThisItemAsChosen.get(currentAnswer));
        // Confirm
        viewModel.getAnswerByUser(currentAnswer);
        assertTrue("After confirm answer should be displayed", viewModel.showRightAnswer.get());
    }

    @Test
    public void whenMultiplyChoicesIsMadeSelectedOnlyLastAndNoAnswerIsShowing() throws Exception {
        // Choose
        viewModel.getAnswerByUser(0);
        viewModel.getAnswerByUser(1);
        viewModel.getAnswerByUser(2);
        viewModel.getAnswerByUser(3);
        assertFalse(viewModel.animateThisItemAsChosen.get(0));
        assertFalse(viewModel.animateThisItemAsChosen.get(1));
        assertFalse(viewModel.animateThisItemAsChosen.get(2));
        assertTrue(viewModel.animateThisItemAsChosen.get(3));
        assertFalse(viewModel.showRightAnswer.get());
    }

    @Test
    public void madeMultipleChoicesAndGiveAnswer() throws Exception {
        // Choose
        viewModel.getAnswerByUser(0);
        viewModel.getAnswerByUser(3);
        viewModel.getAnswerByUser(1);
        viewModel.getAnswerByUser(2);
        viewModel.getAnswerByUser(1);
        assertFalse(viewModel.showRightAnswer.get());
        viewModel.getAnswerByUser(1);
        assertTrue(viewModel.showRightAnswer.get());
        assertFalse(viewModel.animateThisItemAsChosen.get(0));
        assertFalse(viewModel.animateThisItemAsChosen.get(2));
        assertFalse(viewModel.animateThisItemAsChosen.get(3));
        assertTrue(viewModel.animateThisItemAsChosen.get(1));
    }

    @Test
    public void animateButtonAsChosen() {
        Button fakeButton = mock(Button.class);
        GameViewModel.setAnimation(fakeButton, true, false);

    }

}