package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.interfaces.ClassSetupListener;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.ui.settings.DialogClasses;
import com.natalizioapps.monsignordoyle.ui.settings.DialogLunch;
import com.natalizioapps.monsignordoyle.ui.settings.DialogSchools;
import com.natalizioapps.monsignordoyle.ui.views.AvatarListItem;
import com.natalizioapps.monsignordoyle.utils.VisualUtils;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

/**
 * Created by Reno on 15-07-13.
 */
public class ModuleClassSetup extends Fragment implements View.OnTouchListener {

    private ClassSetupListener listener;

    View mView;

    @InjectView(R.id.classetup_b_school) AvatarListItem bSchool;

    @InjectView(R.id.classetup_ll_animation) View llAnimation;
    @InjectView(R.id.classetup_ll_lunch) View llLunch;

    @InjectViews({R.id.classetup_b_period1, R.id.classetup_b_period2,
            R.id.classetup_b_period3, R.id.classetup_b_period4}) AvatarListItem[] bPeriods;

    @InjectView(R.id.classetup_b_lunch) AvatarListItem bLunch;

    /**
     * FLag are set to true after each piece of information has been inputted
     * 0     , 1 , 2 , 3 , 4 , 5
     * School, P1, P2, P3, P4, Lunch
     */
    boolean[] FLAGS = new boolean[6];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mView = inflater.inflate(R.layout.module_classsetup, container, false);
        ButterKnife.inject(this, mView);

        setOnClickListeners();
        setOnTouchListeners();

        refreshSchool();
        refreshClasses();

        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 5; i++) {
            FLAGS[i] = false;
        }

        if (ClassSetupListener.class.isInstance(getParentFragment()))
            listener = (ClassSetupListener) getParentFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (ClassSetupListener.class.isInstance(activity))
                listener = (ClassSetupListener) activity;
        } catch (ClassCastException castException) {
            Log.d("WCDSB", castException.toString());
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

    private void setOnTouchListeners() {
        // If it is the WCDSB APP, allow the user to select their school
        if (PrefSingleton.getInstance().isWCDSBapp())
            bSchool.setOnTouchListener(this);

        for (AvatarListItem bPeriod : bPeriods)
            bPeriod.setOnTouchListener(this);
        bLunch.setOnTouchListener(this);
    }

    private void setOnClickListeners() {
        // If it is the WCDSB APP, allow the user to select their school
        if (PrefSingleton.getInstance().isWCDSBapp()) {
            bSchool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show the preference dialog to choose a school
                    DialogSchools dialog = new DialogSchools(getActivity());
                    // Animate the changes only after dismiss
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            refreshSchool();
                        }
                    });
                    dialog.show();
                }
            });
        }

        // Add Listeners to each of the four period buttons
        for (int i = 0; i < 4; i++) {
            AvatarListItem bPeriod = bPeriods[i];
            final int period = i + 1;
            bPeriod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogClasses dialog = new DialogClasses(getActivity(), period);
                    // Animate the changes only after dismiss
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            refreshClasses();
                        }
                    });
                    dialog.show();
                }
            });
        }

        bLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLunch dialog = new DialogLunch(getActivity());
                // Animate the changes only after dismiss
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        int lunch = PrefSingleton.getInstance().getLunch();
                        if (lunch == 1) {
                            bLunch.setSubText("First Lunch");
                        } else {
                            bLunch.setSubText("Second Lunch");
                        }
                        FLAGS[5] = true;
                        refreshFinish();
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * Called after every dialog, to check if all information has been filled
     * If so, activate the finish button
     */
    private void refreshFinish() {
        if (FLAGS[0] && FLAGS[1] && FLAGS[2] && FLAGS[3] && FLAGS[4] && FLAGS[5]) {
            Log.d("WCDSB", "We tryna refresh");
            listener.allDataEntered();
        }
    }

    /**
     * Check if a school has been chosen
     * If so, expand the other categories for input
     */
    private void refreshSchool() {
        // Check if a school has now been selected
        // If not do not expand other layouts
        HighSchools school = PrefSingleton.getInstance().getHighSchool();

        if (school != HighSchools.UNDEF) {
            // A school has been chosen, yay!
            FLAGS[0] = true;

            // Change the AvatarListItem to reflect the user's choice
            if (school == HighSchools.MONSIGNORDOYLE) {
                bSchool.setAvatar(R.drawable.sch_mondoyle);
                bSchool.setAvatarColor(getResources().getColor(R.color.material_red_400));
                bSchool.setSubText(getString(R.string.school_mondoyle));
            } else if (school == HighSchools.STBENEDICTS) {
                bSchool.setAvatar(R.drawable.sch_benedicts);
                bSchool.setAvatarColor(getResources().getColor(R.color.material_indigo_300));
                bSchool.setSubText(getString(R.string.school_stbenedict));
            } else if (school == HighSchools.STMARYS) {
                bSchool.setAvatar(R.drawable.sch_marys);
                bSchool.setAvatarColor(getResources().getColor(R.color.material_blue_400));
                bSchool.setSubText(getString(R.string.school_stmary));
            } else if (school == HighSchools.STDAVIDS) {
                bSchool.setAvatar(R.drawable.sch_davids);
                bSchool.setAvatarColor(getResources().getColor(R.color.material_green_400));
                bSchool.setSubText(getString(R.string.school_stdavid));
            } else if (school == HighSchools.RESURRECTION) {
                bSchool.setAvatar(R.drawable.sch_resurrection);
                bSchool.setAvatarColor(getResources().getColor(R.color.material_red_400));
                bSchool.setSubText(getString(R.string.school_resurrection));
            }

            listener.schoolChanged();

            // If St. Mary's is chosen, show lunch period button
            if (school == HighSchools.STMARYS) {
                if (llLunch.getVisibility() == View.GONE)
                    VisualUtils.expand(llLunch);
            } else {
                if (llLunch.getVisibility() == View.VISIBLE)
                    VisualUtils.collapse(llLunch);
                // Lunch period is not needed, so skip this step
                FLAGS[5] = true;
            }

            // If not already expanded, show the other sections
            if (llAnimation.getVisibility() == View.GONE) {
                VisualUtils.expand(llAnimation);
            }
        }
    }

    /**
     * Update the details for all classes, but only if a name has been entered
     * If acceptable, FLAG it as entered
     */
    private void refreshClasses() {
        for (int i = 0; i < 4; i++) {
            int period = i + 1;
            SchoolClass c = PrefSingleton.getInstance().getClass(period);
            // Check that the name is not null
            if (c.getName() != null) {
                FLAGS[period] = true;
                AvatarListItem bPeriod = bPeriods[i];
                bPeriod.setSubText(c.getName());
                bPeriod.setAvatarColor(c.getColor());
            }
        }

        // Also refresh lunch button
        int lunch = PrefSingleton.getInstance().getLunch();
        if (lunch == 1) {
            bLunch.setSubText("First Lunch");
        } else {
            bLunch.setSubText("Second Lunch");
        }
        refreshFinish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
