package com.natalizioapps.monsignordoyle.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.BaseActivity;
import com.natalizioapps.monsignordoyle.ui.settings.DialogClasses;
import com.natalizioapps.monsignordoyle.ui.views.ClassCard;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 14-12-09.
 */
public class ActivitySchedule extends BaseActivity implements ClassCard.ClassesCardCallback {

    @InjectView(R.id.card_class_p1) ClassCard cardP1;
    @InjectView(R.id.card_class_p2) ClassCard cardP2;
    @InjectView(R.id.card_class_p3) ClassCard cardP3;
    @InjectView(R.id.card_class_p4) ClassCard cardP4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);
        ButterKnife.inject(this);

        // Initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_schedule));
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                actionBar.setElevation(4);
            }
        }

        refreshSchedule();

        cardP1.setCallback(this);
        cardP2.setCallback(this);
        cardP3.setCallback(this);
        cardP4.setCallback(this);
    }

    private void refreshSchedule() {
        cardP1.setClass(1);
        cardP2.setClass(2);
        cardP3.setClass(3);
        cardP4.setClass(4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Go to the Entries List Activity
            startActivity(new Intent(ActivitySchedule.this, ActivityMainScreen.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                    addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivitySchedule.this, ActivityMainScreen.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }


    @Override
    public void onCardEdit(int period) {
        DialogClasses dialog = new DialogClasses(this, period);
        // Animate the changes only after dismiss
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshSchedule();
            }
        });
        dialog.show();
    }
}
