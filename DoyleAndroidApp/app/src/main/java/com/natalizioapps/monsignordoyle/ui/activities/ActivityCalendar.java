package com.natalizioapps.monsignordoyle.ui.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-07-10.
 */
public class ActivityCalendar extends BaseActivity {

    @InjectView(R.id.calendar_webview) WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);
        ButterKnife.inject(this);

        initToolbar();

        // Load the webview with the calendar for the school
        // Javascript must be enabled for the interactive google calendar
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(SchoolConstants.getSchoolCalendarUrl(false));
    }

    private void initToolbar() {
        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Calendar");
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_calendar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Go to the Entries List Activity
            startActivity(new Intent(ActivityCalendar.this, ActivityMainScreen.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        } else if (id == R.id.action_browser) {
            // On action open the current webView Url in the real browser
            Uri uri = Uri.parse(webView.getUrl());
            Intent link = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(link);
            } catch (ActivityNotFoundException e) {
                // We cannot open a file, so instead request the web link to the calendar
                uri = Uri.parse(SchoolConstants.getSchoolCalendarUrl(true));
                link = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(link);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityCalendar.this, ActivityMainScreen.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }

}
