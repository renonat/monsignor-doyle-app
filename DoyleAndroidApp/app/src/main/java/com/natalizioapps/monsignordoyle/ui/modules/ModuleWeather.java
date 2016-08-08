package com.natalizioapps.monsignordoyle.ui.modules;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.network.DownloadWebpageTask;
import com.natalizioapps.monsignordoyle.utils.NetworkUtils;
import com.natalizioapps.monsignordoyle.utils.MathUtils;
import com.natalizioapps.monsignordoyle.utils.SchoolConstants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 15-06-30.
 */
public class ModuleWeather extends Fragment implements View.OnTouchListener, View.OnClickListener {

    @InjectView(R.id.module_weather_detail) TextView mTemp;
    @InjectView(R.id.module_weather_image) ImageView mImage;

    View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View mBaseView = inflater.inflate(R.layout.module_weather, container, false);
        ButterKnife.inject(this, mBaseView);

        // Download the weather if we have internet access
        if (NetworkUtils.canFetchData(getActivity())) {
            new DownloadWeatherTask(getActivity(), R.string.file_weather)
                    .execute(SchoolConstants.getSchoolWeatherLink());
        }

        // Set the actions for clicking the view
        mBaseView.setOnTouchListener(this);
        mBaseView.setOnClickListener(this);

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
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create an AlertDialog to allow the user to confirm calling the school
        builder.setTitle("Open The Weather Network?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open up the link to the weather network webpage
                Uri uri = Uri.parse("http://mw.theweathernetwork.com/next24hours/caon0109");
                Intent goToWeather = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToWeather);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(mBaseView, "Couldn't go to The Weather Network",
                            Snackbar.LENGTH_LONG).show(); // Don’t forget to show!
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class DownloadWeatherTask extends DownloadWebpageTask {

        public DownloadWeatherTask(Context context, int fileName) {
            super(context, fileName);
        }

        @Override
        protected void onPostExecute(String result) {
            // When we refresh the data, refresh the image and temp
            // Override the default save to file as we want to work directly with the data
            refreshWeather(result);
        }
    }

    private void refreshWeather(String data) {
        if (mTemp != null) {
            mTemp.setText(getTemperature(data));
        }
        if (mImage != null) {
            mImage.setImageDrawable(getImage(data));
        }
    }

    private String getTemperature(String data) {
        return parseFileForTemp(data);
    }


    private String parseFileForTemp(String file) {
        try {
            //TODO: Oops something wrong with the below line
            JSONObject obj = new JSONObject(file);
            String weather = String.valueOf(obj.getJSONObject("main").getDouble("temp"));
            double degrees = Double.parseDouble(weather);
            return String.valueOf((int) MathUtils.round(degrees, 0)) + "°C";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Drawable getImage(String data) {
        String iconname = getImageName(data);
        if ("01n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_moon);
        } else if ("02d".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_partly_cloudy_day);
        } else if ("02n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_partly_cloudy_night);
        } else if ("03d".equals(iconname) || "03n".equals(iconname) ||
                "04d".equals(iconname) || "04n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_clouds);
        } else if ("09d".equals(iconname) || "09n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_little_rain);
        } else if ("10d".equals(iconname) || "10n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_rain);
        } else if ("11d".equals(iconname) || "11n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_storm);
        } else if ("13d".equals(iconname) || "13n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_snow);
        } else if ("50d".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_fog_day);
        } else if ("50n".equals(iconname)) {
            return getResources().getDrawable(R.drawable.weather_fog_night);
        } else {
            return getResources().getDrawable(R.drawable.weather_sun);
        }
    }

    @Nullable
    private String getImageName(String data) {
        JSONObject obj;
        try {
            obj = new JSONObject(data);
            return obj.getJSONArray("weather").getJSONObject(0).getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
