package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.ui.activities.ActivityMap;
import com.natalizioapps.monsignordoyle.ui.views.AvatarListItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-07-01.
 */
public class ModuleSchoolMap extends Fragment implements OnTouchListener {
    @InjectView(R.id.module_schoolmap) AvatarListItem mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.module_schoolmap, container, false);
        ButterKnife.inject(this, mBaseView);

        mMap.setOnTouchListener(this);
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityMap.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
