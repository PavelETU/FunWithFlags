package com.wordpress.lonelytripblog.funwithflags.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wordpress.lonelytripblog.funwithflags.R
import kotlinx.android.synthetic.main.fragment_info.view.*

const val ALL_FLAGS_LEARNT_FLAG = "all_flags_learnt"

class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        val allFlagsLearnt = arguments?.getBoolean(ALL_FLAGS_LEARNT_FLAG) ?: true
        view.flags_info.text = getString(
                if (allFlagsLearnt) R.string.you_learnt_all else R.string.you_learnt_not_all)
        // Inflate the layout for this fragment
        return view
    }


}
