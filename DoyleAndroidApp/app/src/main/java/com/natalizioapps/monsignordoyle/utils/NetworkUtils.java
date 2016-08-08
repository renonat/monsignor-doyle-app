package com.natalizioapps.monsignordoyle.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Reno on 14-08-30.
 */
public class NetworkUtils {

    //TODO: COMMENT

    /**
     * This class checks to see if data can be fetched using the current network
     * This is in place as a workaround for when the system is connected to a network
     * but has no internet access, for example on the old WCDSB wifi network that required a
     * login before the internet was accessible (a.k.a. a walled garden network)
     * @param _context {Context} : the current context
     * @return {boolean} : can we fetch data
     */
    public static boolean canFetchData(Context _context) {
        ConnectivityManager connMgr = (ConnectivityManager) _context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream) throws IOException {
        return new Scanner(stream, "UTF-8").useDelimiter("\\A").next();
    }

    public static class PingGoogleTask extends AsyncTask<Void, Void, Boolean> {

        private static final String mWalledGardenUrl = "http://clients3.google.com/generate_204";
        private static final int WALLED_GARDEN_SOCKET_TIMEOUT_MS = 10000;

        @Override
        protected Boolean doInBackground(Void... params) {
            return network();
        }

        private boolean network() {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mWalledGardenUrl); // "http://clients3.google.com/generate_204"
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setConnectTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
                urlConnection.setReadTimeout(WALLED_GARDEN_SOCKET_TIMEOUT_MS);
                urlConnection.setUseCaches(false);
                urlConnection.getInputStream();
                // We got a valid response, but not from the real google
                return urlConnection.getResponseCode() != 204;
            } catch (IOException e) {
                Log.d("WCDSB", "Walled garden check - probably not a portal: exception " + e);
                return false;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

    }
}
