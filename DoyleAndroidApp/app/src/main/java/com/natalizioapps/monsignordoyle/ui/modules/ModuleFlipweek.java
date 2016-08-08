package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.utils.singletons.FlipweekSingleton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-06-30.
 */
public class ModuleFlipweek extends Fragment {

    @InjectView(R.id.module_flipweek_status) TextView mStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.module_flipweek, container, false);
        ButterKnife.inject(this, mBaseView);

        if (FlipweekSingleton.getInstance().getFlipweekStatus()) {
            mBaseView.setBackgroundColor(getResources().getColor(R.color
                    .material_green_500));
            mStatus.setText("Yes");
        }

        return mBaseView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
