package com.wordpress.lonelytripblog.funwithflags;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;

import com.wordpress.lonelytripblog.funwithflags.data.CallbackForTimer;
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity;
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo;
import com.wordpress.lonelytripblog.funwithflags.util.Counter;
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Павел on 04.03.2018.
 */
@RunWith(JUnit4.class)
public class GameViewModelTest {

    @Rule
    public InstantTaskExecutorRule ruleForTestingArchitectureComponents = new InstantTaskExecutorRule();
    @Mock
    private GameRepo gameRepo;
    @Mock
    private Counter counter;
    private GameViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        GameEntity currentGameEntity = new GameEntity("http://www.countryflags.io/de/shiny/64.png",
                new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3);
        MutableLiveData<GameEntity> result = new MutableLiveData<>();
        result.setValue(currentGameEntity);
        when(gameRepo.getLiveDataForGame()).then(invocation -> result);
        viewModel = new GameViewModel(gameRepo, counter);
    }

    @Test
    public void showRightAnswerAfterAnythingChosen() {
        doAnswer((Answer<Void>) invocation -> null).when(counter).startCounter(any(CallbackForTimer.class));
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
    public void whenMultiplyChoicesIsMadeSelectedOnlyLastAndNoAnswerIsShowing() {
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
    public void madeMultipleChoicesAndGiveAnswer() {
        doAnswer((Answer<Void>) invocation -> null).when(counter).startCounter(any(CallbackForTimer.class));
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
    public void repoRequestedAfterQuestionIsAnswered() {
        // Call onTimerStop right after startCounter was called (disable animation)
        doAnswer((Answer<Void>) invocation -> {
            CallbackForTimer callback = invocation.getArgument(0);
            callback.doOnTimerStop();
            return null;
        }).when(counter).startCounter(any(CallbackForTimer.class));
        viewModel.getAnswerByUser(0);
        viewModel.getAnswerByUser(0);
        verify(gameRepo, times(1)).requestNewFlags();
    }

}