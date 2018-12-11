package com.wordpress.lonelytripblog.funwithflags.ui


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.di.InjectableFragment
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import com.wordpress.lonelytripblog.funwithflags.viewmodels.GameViewModel
import kotlinx.android.synthetic.main.fragment_info.view.*
import javax.inject.Inject

const val FROM_GAME_FRAGMENT = "from_game_fragment"
const val AFTER_ALL_FLAGS_WERE_REVIEWED = "all_flags_were_reviewed"

class InfoFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        val calledFromGameFragment = arguments?.getBoolean(FROM_GAME_FRAGMENT) ?: false
        val allFlagsWereReviewed = arguments?.getBoolean(AFTER_ALL_FLAGS_WERE_REVIEWED) ?: false
        viewModel.amountOfLearntAndLeftFlags.observe(this, Observer {
            it?.also {
                if (calledFromGameFragment) {
                    if (it.second == 0) {
                        view.flags_info.text = getString(R.string.recap_flags_all_flags_learnt, it.first)
                        view.continue_btn.setOnClickListener {
                            navigationController.callRecapGameAsFromNavMenu.invoke()
                        }
                    } else {
                        navigationController.navigateToNewGameFragment()
                    }
                } else if (allFlagsWereReviewed) {
                    view.flags_info.text = getString(R.string.recap_flags_all_flags_reviewed, it.first)
                    view.continue_btn.setOnClickListener {
                        navigationController.navigateToRecapFragment()
                    }
                } else {
                    when {
                        it.second == 0 -> {
                            view.flags_info.text = getString(R.string.recap_flags_all_flags_learnt, it.first)
                            view.continue_btn.setOnClickListener {
                                navigationController.navigateToRecapFragment()
                            }
                        }
                        it.first == 0 -> {
                            view.flags_info.text = getString(R.string.recap_flags_no_flags_learnt, it.second)
                            view.continue_btn.text = getString(R.string.start_game)
                            view.continue_btn.setOnClickListener {
                                navigationController.callNewGameAsFromNavMenu.invoke()
                            }
                        }
                        else -> {
                            view.flags_info.text = getString(R.string.recap_flags_flags_partially_learnt,
                                    it.first, it.first + it.second, it.second)
                            view.continue_btn.setOnClickListener {
                                navigationController.navigateToRecapFragment()
                            }
                        }
                    }
                }
                viewModel.amountOfLearntAndLeftFlags.removeObservers(this)
            }
        })
        if (!calledFromGameFragment) {
            view.continue_btn.setOnClickListener { navigationController.navigateToRecapFragment() }
        }
        return view
    }


}
