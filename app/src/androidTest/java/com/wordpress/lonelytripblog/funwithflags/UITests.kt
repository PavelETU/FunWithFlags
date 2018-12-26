package com.wordpress.lonelytripblog.funwithflags

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.wordpress.lonelytripblog.funwithflags.data.GameEntity
import com.wordpress.lonelytripblog.funwithflags.data.GameRepo
import com.wordpress.lonelytripblog.funwithflags.ui.GameFragment
import com.wordpress.lonelytripblog.funwithflags.util.CallbackForTimer
import com.wordpress.lonelytripblog.funwithflags.util.Counter
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class UITests {

    private val fakeGameEntity = MutableLiveData<GameEntity>()
    private lateinit var fakeViewModel: GameViewModel
    private lateinit var navigationController: NavigationController

    @Before
    fun init() {
        val gameFragment = GameFragment()
        val fakeRepo = mock<GameRepo>(GameRepo::class.java)
        `when`<LiveData<GameEntity>>(fakeRepo.getUnknownCountryGameEntity()).thenReturn(fakeGameEntity)
        fakeViewModel = GameViewModel(fakeRepo, object : Counter {
            override fun startCounter(callback: CallbackForTimer) {
                callback.doOnTimerStop()
            }
        })
        gameFragment.viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return fakeViewModel as T
            }
        }
        navigationController = mock<NavigationController>(NavigationController::class.java)
        gameFragment.navigationController = navigationController
        val activityScenario = ActivityScenario.launch(ActivityToTestFragments::class.java)
        activityScenario.onActivity { activity -> activity.setFragment(gameFragment) }
    }

    @Test
    fun verifyThatDataCorrectlySet() {
        val countriesNames = ArrayList<String>()
        countriesNames.add("China")
        countriesNames.add("Netherlands")
        countriesNames.add("Poland")
        countriesNames.add("Dominican Republic")
        fakeGameEntity.postValue(GameEntity(R.drawable.germany, countriesNames, 0))
        // TODO refactor to countDownLatch
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withId(R.id.button)).check(ViewAssertions.matches(withText(countriesNames[0])))
        onView(withId(R.id.button2)).check(ViewAssertions.matches(withText(countriesNames[1])))
        onView(withId(R.id.button3)).check(ViewAssertions.matches(withText(countriesNames[2])))
        onView(withId(R.id.button4)).check(ViewAssertions.matches(withText(countriesNames[3])))
    }

    @Test
    fun verifyThatDataCorrectlySetChanges() {
        val countriesNames = ArrayList<String>()
        countriesNames.add("China")
        countriesNames.add("Netherlands")
        countriesNames.add("Poland")
        countriesNames.add("Dominican Republic")
        fakeGameEntity.postValue(GameEntity(R.drawable.us, countriesNames, 2))
        // TODO refactor to countDownLatch
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        countriesNames.clear()
        countriesNames.add("Ireland")
        countriesNames.add("Cyprus")
        countriesNames.add("Mexico")
        countriesNames.add("Cuba")
        fakeGameEntity.postValue(GameEntity(R.drawable.thailand, countriesNames, 3))
        // TODO refactor to countDownLatch
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withId(R.id.button)).check(ViewAssertions.matches(withText(countriesNames[0])))
        onView(withId(R.id.button2)).check(ViewAssertions.matches(withText(countriesNames[1])))
        onView(withId(R.id.button3)).check(ViewAssertions.matches(withText(countriesNames[2])))
        onView(withId(R.id.button4)).check(ViewAssertions.matches(withText(countriesNames[3])))
    }

}
