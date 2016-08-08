package com.natalizioapps.monsignordoyle.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.WindowManager;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.activities.ActivityWelcome;
import com.natalizioapps.monsignordoyle.utils.SetupUtils;
import com.natalizioapps.monsignordoyle.utils.ThemeUtils;
import com.natalizioapps.monsignordoyle.utils.singletons.HotspotDataSingleton;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

/**
 * Created by Reno on 14-12-31.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the singletons for use throughout the application
        PrefSingleton.getInstance().Initialize(this);
        HotspotDataSingleton.getInstance().Initialize(this);

        // This class is only in the product Flavors, not in the main app
        // This updates the settings dependant on the Flavor (school)
        SetupUtils.updateSchool();

        // Show the welcome screen if never shown before
        if (PrefSingleton.getInstance().getShowWelcome()) {
            startActivity(new Intent(BaseActivity.this, ActivityWelcome.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                            .FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }

        // Now do the usual activity methods
        super.onCreate(savedInstanceState);

        refreshTheme();
    }

    /**
     * Change the theme based on the current selected high school
     */
    private void refreshTheme() {
        setTheme(ThemeUtils.themeForSchool());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            int color = typedValue.data;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(color);
        }
    }

}
