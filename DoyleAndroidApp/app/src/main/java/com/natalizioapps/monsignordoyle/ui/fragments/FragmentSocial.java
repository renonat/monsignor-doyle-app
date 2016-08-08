package com.natalizioapps.monsignordoyle.ui.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.adapters.SocialAdapter;
import com.natalizioapps.monsignordoyle.network.TwitterAsync;
import com.natalizioapps.monsignordoyle.objects.Tweet;
import com.natalizioapps.monsignordoyle.utils.ObjectSerializer;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;

import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

/**
 * Created by Reno on 15-07-01.
 */
public class FragmentSocial extends Fragment implements View.OnTouchListener {

    @InjectViews({R.id.social_button_one, R.id.social_button_two,
            R.id.social_button_three}) Button[] buttons;
    @InjectView(R.id.social_list) ListView socialList;

    SchoolConstants.SocialAccount account = new SchoolConstants.SocialAccount();

    List<Tweet> socialTweets;

    LocalAsync async;

    public FragmentSocial() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_social, container, false);
        ButterKnife.inject(this, rootView);

        // Deserialize the account info passed from the activity
        String strtext = getArguments().getString("account");
        ArrayList<String> serAccount = null;
        try {
            serAccount = (ArrayList<String>) ObjectSerializer.deserialize(strtext);
            account.setTitle(serAccount.get(0));
            account.setDataurl(serAccount.get(1));
            account.setSocialLinks((ArrayList<Pair<String, String>>)
                    ObjectSerializer.deserialize(serAccount.get(2)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initButtons();

        async = new LocalAsync();
        async.setUrl(account.getDataurl());
        async.execute();

        return rootView;
    }

    private void initButtons() {
        if (account != null) {
            // For each button (if we have enough links)
            for (int i = 0; i < 3; i++) {
                if (i < account.getSocialLinks().size()) {
                    // Set the button text and link to that from the social media account
                    final Pair<String, String> link = account.getSocialLinks().get(i);
                    buttons[i].setText(link.getValue0());
                    buttons[i].setTextColor(Color.WHITE);
                    buttons[i].setVisibility(View.VISIBLE);
                    buttons[i].setOnTouchListener(this);
                    buttons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(link.getValue1());
                            Intent goToWebsite = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                getActivity().startActivity(goToWebsite);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getActivity(), "Couldn't go to " + link.getValue1(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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

    private class LocalAsync extends TwitterAsync {
        @Override
        protected void onPostExecute(List<Tweet> tweets) {
            super.onPostExecute(tweets);
            if (tweets != null) {
                socialTweets = tweets;
                socialList.setAdapter(new SocialAdapter(getActivity(), socialTweets));
                socialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        // Create an AlertDialog to allow the user to confirm opeing the tweet
                        builder.setMessage(socialTweets.get(position).getContent())
                                .setTitle("Open in Twitter?");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse(socialTweets.get(position).getLink());
                                Intent action = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    startActivity(action);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(getActivity(), "Could not open tweet.",
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
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        async.cancel(true);
    }
}
