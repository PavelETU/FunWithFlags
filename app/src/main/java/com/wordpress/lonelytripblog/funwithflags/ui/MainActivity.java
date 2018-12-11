package com.wordpress.lonelytripblog.funwithflags.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.wordpress.lonelytripblog.funwithflags.R;
import com.wordpress.lonelytripblog.funwithflags.util.NavigationController;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HasSupportFragmentInjector {

    private ActionBarDrawerToggle mDrawerToggle;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    NavigationController navigationController;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final LinearLayout toolbarWithMainContent = findViewById(R.id.toolbar_and_main_content);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = findViewById(R.id.navigation_container);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                int width = drawerView.getWidth();
                if (width == 0) {
                    ViewTreeObserver observer = drawerView.getViewTreeObserver();
                    if (observer.isAlive()) {
                        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int viewWidth = drawerView.findViewById(R.id.navigation_container).getWidth();
                                float moveFactor = viewWidth * slideOffset;
                                toolbarWithMainContent.setTranslationX(moveFactor);

                                if (Build.VERSION.SDK_INT < 16) {
                                    drawerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    drawerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            }
                        });
                    }
                } else {
                    float moveFactor = width * slideOffset;
                    toolbarWithMainContent.setTranslationX(moveFactor);
                }
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home_page);

        if (getSupportFragmentManager().findFragmentById(R.id.main_content) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, new HomeFragment()).commit();
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            mDrawerToggle.onDrawerSlide(drawer, 1);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationController.navigateToFragmentByMenuId(item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
