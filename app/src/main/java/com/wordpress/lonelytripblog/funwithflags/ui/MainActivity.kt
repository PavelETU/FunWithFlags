package com.wordpress.lonelytripblog.funwithflags.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.wordpress.lonelytripblog.funwithflags.R
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector, NavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var navigationController: NavigationController

    override fun androidInjector(): AndroidInjector<Any> = fragmentDispatchingAndroidInjector

    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val toolbarWithMainContent = findViewById<LinearLayout>(R.id.toolbar_and_main_content)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.navigation_container)

        val mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        mDrawerToggle = object : ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val width = drawerView.width
                if (width == 0) {
                    val observer = drawerView.viewTreeObserver
                    if (observer.isAlive) {
                        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                val viewWidth = drawerView.findViewById<View>(R.id.navigation_container).width
                                val moveFactor = viewWidth * slideOffset
                                toolbarWithMainContent.translationX = moveFactor

                                if (Build.VERSION.SDK_INT < 16) {
                                    drawerView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                                } else {
                                    drawerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                }
                            }
                        })
                    }
                } else {
                    val moveFactor = width * slideOffset
                    toolbarWithMainContent.translationX = moveFactor
                }
            }
        }

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle)

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.home_page)

        if (supportFragmentManager.findFragmentById(R.id.main_content) == null) {
            supportFragmentManager.beginTransaction().add(R.id.main_content, HomeFragment()).commit()
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle.syncState()
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            mDrawerToggle.onDrawerSlide(drawer, 1f)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationController.navigateToFragmentByMenuId(item.itemId)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}