package com.wordpress.lonelytripblog.funwithflags

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
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
        Mockito.`when`(gameRepo.getUnknownCountryGameEntity()).then { gameEntityLiveData }
        viewModel = GameViewModel(gameRepo, counter)
        viewModel.setRightAnswer(3)
    }

    @Test
    fun showRightAnswerAfterAnythingChosen() {
        // Choose
        val currentAnswer = 0
        viewModel.getAnswerByUser(currentAnswer)
        assertEquals("After first click item should be animated",
                viewModel.firstButtonDrawable.value, R.drawable.btn_background_chosen)
        // Confirm
        viewModel.getAnswerByUser(currentAnswer)
        assertEquals("After confirm answer should be displayed",
                viewModel.firstButtonDrawable.value, R.drawable.btn_background_chosen)
    }

    @Test
    fun whenMultiplyChoicesIsMadeSelectedOnlyLastAndNoAnswerIsShowing() {
        // Choose
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(1)
        viewModel.getAnswerByUser(2)
        viewModel.getAnswerByUser(3)

        assertEquals(viewModel.firstButtonDrawable.value, R.drawable.btn_background)
        assertEquals(viewModel.secondButtonDrawable.value, R.drawable.btn_background)
        assertEquals(viewModel.thirdButtonDrawable.value, R.drawable.btn_background)
        assertEquals(viewModel.fourthButtonDrawable.value, R.drawable.btn_background_chosen)
    }

    @Test
    fun madeMultipleChoicesAndGiveAnswer() {
        // Choose
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(3)
        viewModel.getAnswerByUser(1)
        viewModel.getAnswerByUser(2)
        viewModel.getAnswerByUser(1)
        viewModel.getAnswerByUser(1)

        assertEquals(viewModel.firstButtonDrawable.value, R.drawable.btn_background)
        assertEquals(viewModel.secondButtonDrawable.value, R.drawable.btn_background_chosen)
        assertEquals(viewModel.thirdButtonDrawable.value, R.drawable.btn_background)
        assertEquals(viewModel.fourthButtonDrawable.value, R.drawable.btn_background_right_answer)
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
        doAnswer {
            if (it.arguments.isEmpty()) return@doAnswer null
            viewModel.doOnTimerStop()
            null
        }.`when`(counter).startCounter(viewModel)
    }

}