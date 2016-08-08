package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.objects.Schedule;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.ui.activities.ActivitySchedule;
import com.natalizioapps.monsignordoyle.utils.ClassesUtils;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;

/**
 * Created by Reno on 15-06-30.
 */
public class ModuleSchedule extends Fragment implements View.OnClickListener, View.OnTouchListener {

    @InjectViews({R.id.module_schedule_slot1_name, R.id.module_schedule_slot2_name,
            R.id.module_schedule_slot3_name, R.id.module_schedule_slot4_name,
            R.id.module_schedule_slot5_name}) List<TextView> mScheduleNames;
    @InjectViews({R.id.module_schedule_slot1_time, R.id.module_schedule_slot2_time,
            R.id.module_schedule_slot3_time, R.id.module_schedule_slot4_time,
            R.id.module_schedule_slot5_time}) List<TextView> mScheduleTimes;

    View mBaseView;
    private Handler handler;
    private Runnable minute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.module_schedule, container, false);
        ButterKnife.inject(this, mBaseView);

        mBaseView.setOnTouchListener(this);
        mBaseView.setOnClickListener(this);

        refreshClasses();

        // Refresh the classes module every minute
        handler = new Handler();
        minute = new Runnable() {
            @Override
            public void run() {
                refreshClasses();
                handler.postDelayed(this, 60 * 1000);
            }
        };
        handler.postDelayed(minute, 60 * 1000);

        return mBaseView;
    }


    private void refreshClasses() {
        if (PrefSingleton.getInstance().getHighSchool() != HighSchools.UNDEF) {
            int currPeriod = ClassesUtils.currentPeriod(getActivity());
            @Schedule.PERIOD int[] orderedPeriods = ClassesUtils.orderedPERIODS(getActivity());
            for (int i = 0; i < orderedPeriods.length; i++) {
                TextView name = mScheduleNames.get(i);
                TextView time = mScheduleTimes.get(i);

                SchoolClass c = null;
                @Schedule.PERIOD int period = orderedPeriods[i];

                // If we have an actual period than get it from the prefs
                if (period != Schedule.LUNCH && period != Schedule.NOTATSCHOOL) {
                    c = PrefSingleton.getInstance().getClass(period);
                }

                if (currPeriod == period) {
                    // This is the current period
                    // Therefore make the text bold and colored
                    name.setTypeface(Typeface.DEFAULT_BOLD);
                    time.setTypeface(Typeface.DEFAULT_BOLD);
                    if (c != null) {
                        name.setTextColor(c.getColor());
                        time.setTextColor(c.getColor());
                    } else {
                        name.setTextColor(getResources().getColor(R.color.text));
                        time.setTextColor(getResources().getColor(R.color.text));
                    }
                } else {
                    // This is not the current period
                    // Therefore make the text normal and black
                    name.setTypeface(Typeface.DEFAULT);
                    time.setTypeface(Typeface.DEFAULT);
                    name.setTextColor(getResources().getColor(R.color.text));
                    time.setTextColor(getResources().getColor(R.color.text));
                }

                // If the class is null that means it is lunch time
                if (c != null) {
                    name.setText(c.getName());
                } else {
                    name.setText("Lunch");
                }
                time.setText(ClassesUtils.getStartTimeForPeriod(period, getActivity()));
            }
        }
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
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), ActivitySchedule.class).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(minute);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(minute, 60 * 1000);
        refreshClasses();
    }

    @Override
    public void onStop() {
        handler.removeCallbacks(minute);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
