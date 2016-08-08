package com.natalizioapps.monsignordoyle.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.natalizioapps.monsignordoyle.R;

/**
 * Created by Reno on 15-05-15.
 */
public class FragmentWelcomeMain extends Fragment {

    public FragmentWelcomeMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_welcome_main, container, false);

        return rootView;
    }

}
