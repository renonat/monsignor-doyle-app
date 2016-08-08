package com.natalizioapps.monsignordoyle.ui.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.interfaces.ClassSetupListener;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleClassSetup;
import com.natalizioapps.monsignordoyle.ui.views.AvatarListItem;
import com.natalizioapps.monsignordoyle.utils.ThemeUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-07-03.
 */
public class ActivitySettings extends BaseActivity implements View.OnTouchListener,
        ClassSetupListener {

    @InjectView(R.id.settings_email) AvatarListItem mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_settings));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }

        getFragmentManager().beginTransaction()
                .add(R.id.settings_placeholder_classes, new ModuleClassSetup(), "moduleclasses")
                .commit();


        setListeners();
    }


    private void setListeners() {
        mEmail.setOnTouchListener(this);
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:get2natalizio@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Monsignor Doyle App Feedback v" + getAppVersion());
                startActivity(Intent.createChooser(emailIntent, "Contact Us"));
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Go to the Entries List Activity
            startActivity(new Intent(ActivitySettings.this, ActivityMainScreen.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivitySettings.this, ActivityMainScreen.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    /**
     * Gets the version number of the application
     *
     * @return {@link java.lang.String} : the version-name (number) of the application
     */
    private String getAppVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void allDataEntered() {

    }

    @Override
    public void schoolChanged() {
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

        // Update the background of the ActionBar to our new PrimaryColor
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            final int colorPrimary = typedValue.data;
            actionBar.setBackgroundDrawable(new ColorDrawable(colorPrimary));
        }
    }
}
