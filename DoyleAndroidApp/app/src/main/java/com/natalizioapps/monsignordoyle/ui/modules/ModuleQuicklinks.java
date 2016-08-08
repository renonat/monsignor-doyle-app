package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
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
public class ModuleQuicklinks extends Fragment implements View.OnTouchListener {

    @InjectView(R.id.module_quicklinks_website) AvatarListItem bWebsite;
    @InjectView(R.id.module_quicklinks_classnet) View bClassnet;
    @InjectView(R.id.module_quicklinks_d2l) View bD2l;
    @InjectView(R.id.module_quicklinks_careercruising) View bCareerCrusing;
    @InjectView(R.id.module_quicklinks_cancellations) View bCancellations;
    @InjectView(R.id.module_quicklinks_sis) View bSis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.module_quicklinks, container, false);
        ButterKnife.inject(this, mBaseView);

        setTouchListeners();
        setClickListeners();

        return mBaseView;
    }

    private void setTouchListeners() {
        bWebsite.setOnTouchListener(this);
        bClassnet.setOnTouchListener(this);
        bD2l.setOnTouchListener(this);
        bCancellations.setOnTouchListener(this);
        bCareerCrusing.setOnTouchListener(this);
        bSis.setOnTouchListener(this);
    }

    private void setClickListeners() {
        bWebsite.setSubText(SchoolConstants.getSchoolWebsite());
        bWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(SchoolConstants.getSchoolWebsite());
                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(link);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't access the internet.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bClassnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(SchoolConstants.getSchoolClassnet());
                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(link);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't access the internet.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bD2l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://wcdsb.elearningontario.ca/");
                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(link);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't access the internet.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bCancellations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://bpweb.stswr.ca/Cancellations.aspx");
                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(link);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't access the internet.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bCareerCrusing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www2.careercruising" +
                        ".com/default/cplogin/A015794C-6FCB-4BDA-9AAA-0DB812F56BA0");
                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(link);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't access the internet.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bSis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://sis.wcdsb.ca/sis/");
                Intent link = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(link);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't access the internet.",
                            Toast.LENGTH_SHORT).show();
                }
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
