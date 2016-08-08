package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.views.AvatarListItem;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-06-30.
 */
public class ModuleSchoolPhone extends Fragment implements View.OnTouchListener {

    @InjectView(R.id.module_phone_attendance) AvatarListItem mAttendance;
    @InjectView(R.id.module_phone_div_attendance) View divAtt;

    @InjectView(R.id.module_phone_school) AvatarListItem mPhone;
    @InjectView(R.id.module_phone_div_phone) View divPhn;

    @InjectView(R.id.module_phone_hours) AvatarListItem mHours;
    @InjectView(R.id.module_phone_div_hours) View divHrs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.module_schoolphone, container, false);
        ButterKnife.inject(this, mBaseView);

        // These values are returned based on the school
        // If they are null, the AvatarListItem will be hidden along with its divider
        String schoolphone = SchoolConstants.getSchoolPhone();
        String attendancephone = SchoolConstants.getAttendancePhone();
        String officehours = SchoolConstants.getOfficeHours();

        if (schoolphone != null) {
            mPhone.setSubText(schoolphone);
            initPhoneOnClickListener(schoolphone);
            mPhone.setOnTouchListener(this);
        } else {
            mPhone.setVisibility(View.GONE);
            divPhn.setVisibility(View.GONE);
        }

        if (attendancephone != null) {
            mAttendance.setSubText(attendancephone);
            initAttendanceOnClickListener(attendancephone);
            mAttendance.setOnTouchListener(this);
        } else {
            mAttendance.setVisibility(View.GONE);
            divAtt.setVisibility(View.GONE);
        }

        if (officehours != null) {
            mHours.setSubText(officehours);
        } else {
            mHours.setVisibility(View.GONE);
            divHrs.setVisibility(View.GONE);
        }

        return mBaseView;
    }

    void initPhoneOnClickListener(final String phone) {
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Create an AlertDialog to allow the user to confirm calling the school
                builder.setMessage(phone)
                        .setTitle("Call " + SchoolConstants.getSchoolName() + "?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("tel:" + SchoolConstants.getSchoolPhone());
                        Intent action = new Intent(Intent.ACTION_DIAL, uri);
                        try {
                            startActivity(action);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "Could not open phone.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    void initAttendanceOnClickListener(final String phone) {
        mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Create an AlertDialog to allow the user to confirm calling the school
                builder.setMessage(phone)
                        .setTitle("Call " + SchoolConstants.getSchoolName() + " attendance?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("tel:" + SchoolConstants.getAttendancePhone());
                        Intent action = new Intent(Intent.ACTION_DIAL, uri);
                        try {
                            startActivity(action);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "Could not open phone.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
