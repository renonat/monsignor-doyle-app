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
 * Created by Reno on 15-07-01.
 */

public class ModuleAddress extends Fragment implements View.OnTouchListener {

    @InjectView(R.id.module_address) AvatarListItem mAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.module_address, container, false);
        ButterKnife.inject(this, mBaseView);

        mAddress.setSubText(SchoolConstants.getSchoolAddress());
        mAddress.setOnTouchListener(this);
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Create an AlertDialog to allow the user to confirm calling the school
                builder.setMessage(SchoolConstants.getSchoolAddress())
                        .setTitle("Open in Maps?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(SchoolConstants.getSchoolGMaps());
                        Intent action = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(action);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "Could not open map.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return mBaseView;
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
