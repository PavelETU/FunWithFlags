package com.wordpress.lonelytripblog.funwithflags

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.FrameLayout


class ActivityToTestFragments : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = FrameLayout(this)
        container.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        container.id = R.id.container
        setContentView(container)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment, "TEST")
                .commit()
    }

}
