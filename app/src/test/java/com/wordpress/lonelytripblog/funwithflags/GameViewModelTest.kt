package com.wordpress.lonelytripblog.funwithflags

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.Answer
import java.util.*

class GameViewModelTest {

    @Rule
    @JvmField
    val ruleForTestingArchitectureComponents = InstantTaskExecutorRule()
    @Mock
    private lateinit var gameRepo: GameRepo
    @Mock
    private lateinit var counter: Counter
    private lateinit var viewModel: GameViewModel
    private lateinit var gameEntityLiveData: MutableLiveData<GameEntity>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val currentGameEntity = GameEntity(R.drawable.germany,
                ArrayList(Arrays.asList("Russia", "USA", "Thailand", "Germany")), 3)
        gameEntityLiveData = MutableLiveData()
        gameEntityLiveData.value = currentGameEntity
        Mockito.`when`(gameRepo.getUnknownCountryGameEntity()).then{ gameEntityLiveData }
        viewModel = GameViewModel(gameRepo, counter)
        viewModel.setRightAnswer(3)
    }

    @Test
    fun showRightAnswerAfterAnythingChosen() {
        // Choose
        val currentAnswer = 0
        viewModel.getAnswerByUser(currentAnswer)
        assertTrue("After first click item should be animated",
                viewModel.animateThisItemAsChosen[currentAnswer])
        // Confirm
        viewModel.getAnswerByUser(currentAnswer)
        assertTrue("After confirm answer should be displayed", viewModel.showAsRightAnswer[3])
    }

    @Test
    fun whenMultiplyChoicesIsMadeSelectedOnlyLastAndNoAnswerIsShowing() {
        // Choose
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(1)
        viewModel.getAnswerByUser(2)
        viewModel.getAnswerByUser(3)
        assertFalse(viewModel.animateThisItemAsChosen[0])
        assertFalse(viewModel.animateThisItemAsChosen[1])
        assertFalse(viewModel.animateThisItemAsChosen[2])
        assertTrue(viewModel.animateThisItemAsChosen[3])
        assertFalse(viewModel.showAsRightAnswer[3])
    }

    @Test
    fun madeMultipleChoicesAndGiveAnswer() {
        // Choose
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(3)
        viewModel.getAnswerByUser(1)
        viewModel.getAnswerByUser(2)
        viewModel.getAnswerByUser(1)
        assertFalse(viewModel.showAsRightAnswer[3])
        viewModel.getAnswerByUser(1)
        assertTrue(viewModel.showAsRightAnswer[3])
        assertFalse(viewModel.animateThisItemAsChosen[0])
        assertFalse(viewModel.animateThisItemAsChosen[2])
        assertFalse(viewModel.animateThisItemAsChosen[3])
        assertTrue(viewModel.animateThisItemAsChosen[1])
    }

    @Test
    fun timerStartAfterAnswerIsSet() {
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(0)

        verify<Counter>(counter, times(1)).startCounter(viewModel)
    }

    @Test
    fun repoRequestedOnTimerStop() {
        viewModel.doOnTimerStop()
        verify<GameRepo>(gameRepo, times(1)).requestNewGameEntity()
    }

    @Test
    fun ifUserGetRightAnswerSaveFlagIsCalled() {
        callCallbackForTimerRightAway()
        // Confirm wrong answer
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(0)
        // Confirm right answer
        viewModel.getAnswerByUser(3)
        viewModel.getAnswerByUser(3)

        verify<GameRepo>(gameRepo, times(1)).saveCurrentFlagIntoLearntFlags()
    }

    private fun callCallbackForTimerRightAway() {
        // Call onTimerStop right after startCounter was called (disable animation)
        doAnswer{
            if (it.arguments.isEmpty()) return@doAnswer null
            viewModel.doOnTimerStop()
            null
        }.`when`(counter).startCounter(viewModel)
    }

}