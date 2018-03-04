package com.wordpress.lonelytripblog.funwithflags;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Павел on 04.03.2018.
 */
public class GameViewModelTest {

    GameViewModel viewModel;
    GameEntity currentGameEntity;

    @Before
    public void setUp() {
        currentGameEntity = new GameEntity("http://www.countryflags.io/de/shiny/64.png",
                new ArrayList<>(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3);
        viewModel = new GameViewModel(currentGameEntity);

    }

    @Test
    public void afterRightAnswerObservableWithRightIndexIsCheckAsRight() throws Exception {
        // Choose
        int rightAnswer = currentGameEntity.getRightAnswer();
        viewModel.getAnswerByUser(rightAnswer);
        assertTrue("First, item should be selected then clicked again",
                viewModel.animateThisItemAsChosen.get(rightAnswer));
        // Confirm
        viewModel.getAnswerByUser(rightAnswer);
        assertTrue("Item should be animated as right answer",
                viewModel.animateThisItemAsRightAnswer.get(rightAnswer));
        assertFalse("There is no need to animate already chosen answer as chosen",
                viewModel.animateThisItemAsChosen.get(rightAnswer));
    }

    @Test
    public void afterWrongAnswerItsStillChosenAndRightAnswerIsAnimated() throws Exception {
        int wrongAnswer = currentGameEntity.getRightAnswer() > 0
                ? currentGameEntity.getRightAnswer() - 1 : currentGameEntity.getRightAnswer() + 1;
        // Choose
        viewModel.getAnswerByUser(wrongAnswer);
        assertTrue("First, item should be selected then clicked again",
                viewModel.animateThisItemAsChosen.get(wrongAnswer));
        // Confirm
        viewModel.getAnswerByUser(wrongAnswer);
        assertTrue("Item should be animated as right answer",
                viewModel.animateThisItemAsRightAnswer.get(currentGameEntity.getRightAnswer()));
        assertTrue("Wrong answer is still animated",
                viewModel.animateThisItemAsChosen.get(wrongAnswer));
    }



}