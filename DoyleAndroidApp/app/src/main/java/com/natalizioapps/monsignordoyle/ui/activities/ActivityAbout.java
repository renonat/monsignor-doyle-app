package com.natalizioapps.monsignordoyle.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleAddress;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleQuicklinks;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleSchoolMap;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleSchoolPhone;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;

import butterknife.ButterKnife;

public class ActivityAbout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_about));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }

        initModules();
    }

    private void initModules() {
        getFragmentManager().beginTransaction()
                .add(R.id.about_placeholder_phone, new ModuleSchoolPhone(), "modulephone")
                .commit();

        if (SchoolConstants.moduleSchoolMap()) {
            getFragmentManager().beginTransaction()
                    .add(R.id.about_placeholder_schoolmap, new ModuleSchoolMap(), "moduleschoolmap")
                    .commit();
        }

        getFragmentManager().beginTransaction()
                .add(R.id.about_placeholder_address, new ModuleAddress(), "moduleaddress")
                .commit();

        getFragmentManager().beginTransaction()
                .add(R.id.about_placeholder_quicklinks, new ModuleQuicklinks(), "modulequicklinks")
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Go to the Entries List Activity
            startActivity(new Intent(ActivityAbout.this, ActivityMainScreen.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityAbout.this, ActivityMainScreen.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }

}
