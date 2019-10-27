package com.wordpress.lonelytripblog.funwithflags.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wordpress.lonelytripblog.funwithflags.databinding.GameFragBinding
import com.wordpress.lonelytripblog.funwithflags.util.GameFragmentFactory
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GAME_STATE_IN_PROGRESS
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GAME_STATE_NO_MORE_FLAGS
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GAME_STATE_TRANSFER
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import kotlinx.android.synthetic.main.game_frag.*
import javax.inject.Inject


class GameFragment @Inject constructor(private val viewModelFactory: ViewModelProvider.Factory,
                                       private val navigationController: NavigationController) : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var mGameFragBinding: GameFragBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mGameFragBinding = GameFragBinding.inflate(inflater, container, false)
        mGameFragBinding.lifecycleOwner = this
        return mGameFragBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        mGameFragBinding.gameViewModel = viewModel
        viewModel.gameState.observe(viewLifecycleOwner, Observer<Int> { result ->
            when (result) {
                GAME_STATE_NO_MORE_FLAGS -> navigateToGameInfoFragment()
                GAME_STATE_IN_PROGRESS -> displayGame()
                GAME_STATE_TRANSFER -> hideGame()
            }
        })
    }

    // TODO create new constraint set with info instead of jumping into recap fragment
    private fun navigateToGameInfoFragment() {
        navigationController.navigateToGameInformationFragment(true)
    }

    private fun displayGame() {
        game_group.visibility = View.VISIBLE
    }

    private fun hideGame() {
        game_group.visibility = View.GONE
    }
}
