package com.natalizioapps.monsignordoyle.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.ui.fragments.FragmentSocial;
import com.natalizioapps.monsignordoyle.ui.fragments.FragmentWelcomeMain;
import com.natalizioapps.monsignordoyle.utils.ObjectSerializer;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Reno on 15-07-01.
 */
public class ActivitySocial extends BaseActivity {

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

    SchoolConstants.SocialAccount[] accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social);

        // Create the toolbar which displays the name of the app at the top
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_connect));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }

        accounts = SchoolConstants.getSocialAccounts();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.social_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });

        // Create the tablayout with our tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.social_tablayout);
        for (SchoolConstants.SocialAccount acc : accounts) {
            tabLayout.addTab(tabLayout.newTab().setText(acc.getTitle()));
        }

        // Update tablayout when we swipe the viewpager
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Update viewpager when we tap the tablayout
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));

        // Set the background color of the tablayout to our primary color
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        final int colorPrimary = typedValue.data;
        tabLayout.setBackgroundColor(colorPrimary);
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
            try {
                SchoolConstants.SocialAccount acc = accounts[position];
                FragmentSocial fs = new FragmentSocial();
                Bundle bundle = new Bundle();
                // Serialize the SocialAccount to pass it to the fragment
                ArrayList<String> serAccount = new ArrayList<String>();
                serAccount.add(acc.getTitle());
                serAccount.add(acc.getDataurl());
                serAccount.add(ObjectSerializer.serialize(acc.getSocialLinks()));
                // Add it to the bundle and pass it along
                bundle.putString("account", ObjectSerializer.serialize(serAccount));
                fs.setArguments(bundle);
                return fs;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new FragmentWelcomeMain();

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(ActivitySocial.this, ActivityMainScreen.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivitySocial.this, ActivityMainScreen.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }
}
