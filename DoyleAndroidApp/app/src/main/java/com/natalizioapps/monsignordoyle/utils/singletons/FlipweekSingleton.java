package com.natalizioapps.monsignordoyle.utils.singletons;

import android.content.Context;
import android.util.Log;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.network.DownloadWebpageTask;
import com.natalizioapps.monsignordoyle.utils.NetworkUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A Singleton to keep track of the current flipweek state.
 */
public class FlipweekSingleton {
    private static FlipweekSingleton ourInstance = null;

    private int[] flipweeks = null;
    private boolean updated = false;

    public static FlipweekSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new FlipweekSingleton();
        }
        return ourInstance;
    }

    /**
     * Initialize the Singleton by updating the cache from the remote file.
     *
     * @param c {Context}
     */
    public void Initialize(Context c) {
        if (!updated) {
            new PrivatePingGoogle(c).execute();
            updated = true;
        } else {
            // Try getting the cached flipweek file
            String path = c.getCacheDir().getAbsolutePath() + File.separator + c
                    .getString(R.string.file_flipweek);
            File file = new File(path);

            if (file.exists())
                // If the file exists, read the flipweek data from the cache
                try {
                    String data = NetworkUtils.readIt(new FileInputStream(file));
                    String[] array = data.split(",");
                    flipweeks = new int[array.length];
                    for (int i = 0; i < array.length; i++) {
                        try {
                            flipweeks[i] = Integer.parseInt(array[i]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            failsafeInit();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else {
                // We have no cache, so use the fail safe data
                failsafeInit();
            }
        }
    }

    /**
     * This data is only if they cannot access the internet to update the data
     */
    private void failsafeInit() {
        flipweeks = new int[]{38, 40, 42, 44, 46, 48, 50,
                3, 5, 7, 9, 11, 14, 16, 18, 20, 22, 24, 25};
    }

    /**
     * Returns whether or not the current week is a flipweek based on the week of year.
     *
     * @return {boolean} : flipweek state
     */
    public boolean getFlipweekStatus() {
        // Calculate the week of the year taking into account Sundays
        int weekOfYear = DateTime.now().getWeekOfWeekyear();

        // Increase week of year by one if it is a Sunday, looping at the end of the year
        if (DateTime.now().getDayOfWeek() == DateTimeConstants.SUNDAY) {
            weekOfYear++;
            if (weekOfYear == 52) {
                weekOfYear = 1;
            }
        }

        // Check for the validity of the data
        if (flipweeks == null) {
            failsafeInit();
        }

        for (int i : flipweeks)
            if (i == weekOfYear)
                return true;
        return false;
    }

    /**
     * Get updated flipweek data from GitHub
     * This measure allows the flipweek to be updated remotely in case of unexpected error.
     *
     * @param c {Context}
     */
    private void updateFlipweek(Context c) {
        // Download the flipweek data if we have internet access
        if (NetworkUtils.canFetchData(c)) {
            new DownloadTextTask(c, R.string.file_flipweek)
                    .execute("https://raw.githubusercontent.com/renonat/monsignor-doyle-app/master/assets/flipweeks.txt");
        }
    }

    /**
     * Extends {DownloadWebpageTask} for local implementation of file saving
     */
    private class DownloadTextTask extends DownloadWebpageTask {

        Context mContext;
        String mFileName;


        public DownloadTextTask(Context context, int fileName) {
            super(context, fileName);
            mContext = context;
            mFileName = mContext.getString(fileName);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String base = mContext.getCacheDir().getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(base + File.separator + mFileName);
                fos.write(result.getBytes());
                fos.flush();
                fos.close();
                Initialize(mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This checks for a garden wall effect
     * (when the wifi opens it's own page in an effort for you to log in)
     * <p>
     * If we are garden walled, use the fail-safe data
     * If not, update from the internet
     */
    private class PrivatePingGoogle extends NetworkUtils.PingGoogleTask {

        Context _c;

        public PrivatePingGoogle(Context _c) {
            this._c = _c;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                failsafeInit();
                Log.d("WCDSB", "Garden Walled");
            } else {
                updateFlipweek(_c);
                Log.d("WCDSB", "Flipweek updated");
            }
        }
    }
}
