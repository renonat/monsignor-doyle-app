package com.natalizioapps.monsignordoyle.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.interfaces.ClassSetupListener;
import com.natalizioapps.monsignordoyle.ui.activities.ActivityWelcome;
import com.natalizioapps.monsignordoyle.ui.modules.ModuleClassSetup;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-05-15.
 */
public class FragmentWelcomeSetup extends Fragment implements ClassSetupListener {

    View mView;

    @InjectView(R.id.welcome_b_finish) Button bFinish;

    public FragmentWelcomeSetup() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_welcome_setup, container, false);
        ButterKnife.inject(this, mView);

        // Yeah we messed something up here
        getChildFragmentManager().beginTransaction()
                .add(R.id.setup_placeholder_classes, new ModuleClassSetup(), "moduleclasses")
                .commit();

        setOnClickListeners();
        return mView;
    }


    private void setOnClickListeners() {
        bFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When finished, call back to the Welcome Activity
                ((ActivityWelcome) getActivity()).finishWelcome();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void allDataEntered() {
        bFinish.setVisibility(View.VISIBLE);
    }

    @Override
    public void schoolChanged() {

    }
}
