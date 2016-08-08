package com.natalizioapps.monsignordoyle.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.WindowManager;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.fragments.FragmentWelcomeMain;
import com.natalizioapps.monsignordoyle.ui.fragments.FragmentWelcomeSetup;
import com.natalizioapps.monsignordoyle.utils.ThemeUtils;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Reno on 15-05-15.
 */
public class ActivityWelcome extends ActionBarActivity {
    //Cannot extend BaseActivity, or else we have a loop of opening ActivityWelcome

    /**
     * The number of pages (wizard steps) to show.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefSingleton.getInstance().Initialize(this);
        // Change the theme based on the currently selected highschool
        setTheme(ThemeUtils.themeForSchool());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            int color = typedValue.data;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        }

        setContentView(R.layout.activity_welcome);

        // Create the toolbar which displays the name of the app at the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.welcome_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });

        //Bind the ViewPager to the CircleIndicator
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.welcome_indicator);
        defaultIndicator.setViewPager(mPager);
    }

    /**
     * Transition out of the welcome activity and into the main app
     * Update settinsgs so that the Welcome screen is not displayed again
     */
    public void finishWelcome() {
        PrefSingleton.getInstance().setShowWelcome(false);
        startActivity(new Intent(ActivityWelcome.this, ActivityMainScreen.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                        .FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }


    /**
     * A simple pager adapter that represents 2 Fragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new FragmentWelcomeSetup();
                default:
                    return new FragmentWelcomeMain();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    @Override
    public void onBackPressed() {
        // Do nothing so that the user cannot backtrack out of the welcome activity
    }

}
