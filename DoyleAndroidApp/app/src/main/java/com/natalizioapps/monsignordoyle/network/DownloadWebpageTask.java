package com.natalizioapps.monsignordoyle.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.natalizioapps.monsignordoyle.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Uses AsyncTask to create a task away from the main UI thread. This task takes a
 * URL string and uses it to create an HttpUrlConnection. Once the connection
 * has been established, the AsyncTask downloads the contents of the webpage as
 * an InputStream. Finally, the InputStream is converted into a string, which is
 * displayed in the UI by the AsyncTask's onPostExecute method.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {

    private static Context mContext;

    private static String DEBUG_TAG = "DOWNLOAD_WEBPAGE_TASK";

    private static String mFileName = "cache";

    protected DownloadWebpageTask(Context context, int fileName) {
        mContext = context;
        mFileName = mContext.getString(fileName);
    }

    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    //

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves
     * the web page content as a InputStream, which it returns as
     * a string.
     *
     * @param myurl {String} : URL as a String
     * @return {String} : the raw text from the
     * @throws IOException
     */
    protected String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return NetworkUtils.readIt(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

//    @Override
//    protected void onPostExecute(String result) {
//        // Default action is to save the string to a file on the system
//        try {
//            String base = mContext.getCacheDir().getAbsolutePath();
//            FileOutputStream fos = new FileOutputStream(base + File.separator + mFileName);
//            fos.write(result.getBytes());
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}