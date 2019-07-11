package com.wordpress.lonelytripblog.funwithflags

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GAME_STATE_IN_PROGRESS
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GAME_STATE_NO_MORE_FLAGS
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
    private val gameEntityLiveData = MutableLiveData<GameEntity>()
    private val defaultGameEntity = GameEntity(R.drawable.germany,
            listOf("Russia", "USA", "Thailand", "Germany"), 3)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(gameRepo.getUnknownCountryGameEntity()).then { gameEntityLiveData }
        viewModel = GameViewModel(gameRepo, counter)
        viewModel.gameState.observeForever(mock(Observer::class.java) as Observer<Int>)
    }

    @Test
    fun showRightAnswerAfterAnythingChosen() {
        gameEntityLiveData.value = defaultGameEntity
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
        gameEntityLiveData.value = defaultGameEntity
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
        gameEntityLiveData.value = defaultGameEntity
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
        gameEntityLiveData.value = defaultGameEntity
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
        gameEntityLiveData.value = defaultGameEntity
        callCallbackForTimerRightAway()
        // Confirm wrong answer
        viewModel.getAnswerByUser(0)
        viewModel.getAnswerByUser(0)
        // Confirm right answer
        viewModel.getAnswerByUser(3)
        viewModel.getAnswerByUser(3)

        verify<GameRepo>(gameRepo, times(1)).saveCurrentFlagIntoLearntFlags()
    }

    @Test
    fun noMoreFlagsIfGameEntityNull() {
        gameEntityLiveData.value = null

        assertEquals(GAME_STATE_NO_MORE_FLAGS, viewModel.gameState.value)
    }

    @Test
    fun gameInProgressWithValidGameEntity() {
        gameEntityLiveData.value = defaultGameEntity

        assertEquals(GAME_STATE_IN_PROGRESS, viewModel.gameState.value)
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