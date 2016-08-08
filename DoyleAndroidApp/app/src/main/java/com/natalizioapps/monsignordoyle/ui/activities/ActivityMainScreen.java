package com.natalizioapps.monsignordoyle.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleFlipweek;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleSchedule;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleWeather;
import com.natalizioapps.monsignordoyle.utils.NetworkUtils;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;
import com.natalizioapps.monsignordoyle.utils.singletons.FlipweekSingleton;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ActivityMainScreen extends BaseActivity implements View.OnTouchListener {

    @InjectView(R.id.main_b_about)
    Button mButtonAbout;
    @InjectView(R.id.main_b_connect)
    Button mButtonConnect;
    @InjectView(R.id.main_b_calendar)
    Button mButtonCalendar;
    @InjectView(R.id.main_hero)
    ImageView mHeroImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlipweekSingleton.getInstance().Initialize(this);

        setContentView(R.layout.activity_main_screen);
        ButterKnife.inject(this);

        initToolbar();

        initHeroImage();
        initConnect();
        initAbout();
        initCalendar();
        initAnnouncements();
        moduleFlipweek();
        moduleWeather();
        moduleSchedule();
    }

    private void initToolbar() {
        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(SchoolConstants.getSchoolName() + " Student App");
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            mButtonAbout.setElevation(0);
            mButtonConnect.setElevation(0);
            mButtonCalendar.setElevation(0);
        }
    }

    private void initHeroImage() {
        if (PrefSingleton.getInstance().getHighSchool() != HighSchools.UNDEF)
            mHeroImage.setImageDrawable(getResources().getDrawable(SchoolConstants.getHeroImage()));
    }

    private void initAbout() {
        mButtonAbout.setOnTouchListener(this);
        mButtonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMainScreen.this, ActivityAbout.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void initConnect() {
        mButtonConnect.setOnTouchListener(this);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMainScreen.this, ActivitySocial.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void initCalendar() {
        mButtonCalendar.setOnTouchListener(this);
        mButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMainScreen.this, ActivityCalendar.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void initAnnouncements() {

    }

    /**
     * Only adds the Flipweek Module if it is compatible with the current school
     */
    private void moduleFlipweek() {
        if (SchoolConstants.moduleFlipweek()) {
            getFragmentManager().beginTransaction()
                    .add(R.id.main_placeholder_flipweek, new ModuleFlipweek(), "moduleflipweek")
                    .commit();
        }
    }

    /**
     * Only shows the weather if we can access the internet
     */
    private void moduleWeather() {
        if (NetworkUtils.canFetchData(this))
            getFragmentManager().beginTransaction()
                    .add(R.id.main_placeholder_weather, new ModuleWeather(), "moduleweather")
                    .commit();
    }

    private void moduleSchedule() {
        getFragmentManager().beginTransaction()
                .add(R.id.main_placeholder_schedule, new ModuleSchedule(), "moduleschedule")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(ActivityMainScreen.this, ActivitySettings.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Needed else the selector is laggy to update colors
        // This sends the ACTION_DOWN much quicker, making extremely fast response times
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                break;
        }
        return false;
    }
}
