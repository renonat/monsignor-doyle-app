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
 * Created by Reno on 14-08-29.
 */
public class FlipweekSingleton {
    //TODO: COMMENT
    private int[] flipweeks = null;

    private static FlipweekSingleton ourInstance = null;

    public static FlipweekSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new FlipweekSingleton();
        }
        return ourInstance;
    }

    private boolean updated = false;


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
                // If the file exists, read the flipweek data from there
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
               failsafeInit();
            }
        }
    }

    private void failsafeInit() {
        // This data is only if they cannot access the internet to update the data
        flipweeks = new int[]{38, 40, 42, 44, 46, 48, 50,
                3, 5, 7, 9, 11, 14, 16, 18, 20, 22, 24, 25};
    }

    public boolean getFlipweekStatus() {
        // Calculate the week of the year taking into account Sundays
        int weekOfYear = DateTime.now().getWeekOfWeekyear();

        if (DateTime.now().getDayOfWeek() == DateTimeConstants.SUNDAY) {
            weekOfYear++;
            if (weekOfYear == 52) {
                weekOfYear = 1;
            }
        }

        if (flipweeks == null) {
            failsafeInit();
        }

        for (int i : flipweeks)
            if (i == weekOfYear)
                return true;
        return false;
    }

    /**
     * Get updated flipweek data from pastebin
     * This measure is in place in-case I get the flipweek data horribly wrong
     * The paste is saved under the natalizioapps account
     *
     * @param c
     */
    private void updateFlipweek(Context c) {
        // Download the flipweek data if we have internet access
        if (NetworkUtils.canFetchData(c)) {
            new DownloadPastebinTask(c, R.string.file_flipweek)
                    .execute("http://pastebin.com/raw.php?i=4RWpvJFW");
        }
    }

    private class DownloadPastebinTask extends DownloadWebpageTask {

        Context mContext;
        String mFileName;

        public DownloadPastebinTask(Context context, int fileName) {
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

    private class PrivatePingGoogle extends NetworkUtils.PingGoogleTask {

        Context _c;

        public PrivatePingGoogle(Context _c) {
            this._c = _c;
        }

        /*
         * This checks for a garden wall effect
         * (when the wifi opens it's own page in an effort for you to log in)
         *
         * If we are garden walled, use the failsafe data
         * If not, update from the internet
         */
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
