package com.natalizioapps.monsignordoyle.network;

import android.os.AsyncTask;

import com.natalizioapps.monsignordoyle.objects.Tweet;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Reno on 14-10-12.
 * Code from: http://www.dreamincode.net/forums/blog/114/entry-4459-demo-of-twitter-application
 * -only-oauth-authentication-using-java/
 */
public class TwitterAsync extends AsyncTask<String, Void, List<Tweet>> {
    //TODO: COMMENT
    static String bearerToken = null;
    String url;
    String oauthUrl = "https://api.twitter.com/oauth2/token";
    final static int tweetcount = 20;

    public TwitterAsync setUrl(String param) {
        url = param;
        return this;
    }

    @Override
    protected List<Tweet> doInBackground(String... params) {
        List<Tweet> tweets = null;
        try {
            bearerToken = requestBearerToken(oauthUrl);
            tweets = fetchTimelineTweets(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tweets;
    }

    // Encodes the consumer key and secret to create the basic authorization key
    private static String encodeKeys(String consumerKey, String consumerSecret) {
        try {
            String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
            String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");

            String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
            byte[] encodedBytes = Base64.encodeBase64(fullKey.getBytes());
            return new String(encodedBytes);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    // Constructs the request for requesting a bearer token and returns that token as a string
    private static String requestBearerToken(String endPointUrl) throws IOException {
        HttpsURLConnection connection = null;
        String encodedCredentials = encodeKeys("7VI4WpAZbdGlKh7ttUXLKhCnd",
                "wjidcd2FBspxhnXot5FYybdh3dLUV1KcOmITSZfSBs0KPRaKvc");

        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "api.twitter.com");
            connection.setRequestProperty("User-Agent", "Monsignor Doyle");
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;" +
                    "charset=UTF-8");
            connection.setRequestProperty("Content-Length", "29");
            connection.setUseCaches(false);

            connection.connect();

            writeRequest(connection, "grant_type=client_credentials");

            // Parse the JSON response into a JSON mapped object to fetch fields from.
            JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));

            if (obj != null) {
                String tokenType = (String) obj.get("token_type");
                String token = (String) obj.get("access_token");

                return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
            }
            return new String();
        } catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint URL specified.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // Fetches the first tweet from a given user's timeline
    private static List<Tweet> fetchTimelineTweets(String endPointUrl) throws IOException {
        HttpsURLConnection connection = null;

        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Host", "api.twitter.com");
            connection.setRequestProperty("User-Agent", "Monsignor Doyle");
            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            connection.setUseCaches(false);

            connection.connect();

            // Parse the JSON response into a JSON mapped object to fetch fields from.
            JSONArray obj = (JSONArray) JSONValue.parse(readResponse(connection));

            if (obj != null) {
                List<Tweet> list = new ArrayList<Tweet>();
                for (int i = 0; i < tweetcount; i++) {
                    Tweet tweet = new Tweet();
                    tweet.setContent(((JSONObject) obj.get(i)).get("text").toString().trim());
                    tweet.setTime(((JSONObject) obj.get(i)).get("created_at").toString());
                    String id = ((JSONObject) obj.get(i)).get("id_str").toString();
                    String screenname = ((JSONObject) ((JSONObject) obj.get(i))
                            .get("user")).get("screen_name").toString();
                    tweet.setLink(screenname, id);
                    list.add(tweet);
                }
                return list;
            }
            return null;
        } catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint URL specified.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // Writes a request to a connection
    private static boolean writeRequest(HttpsURLConnection connection, String textBody) {
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection
                    .getOutputStream()));
            wr.write(textBody);
            wr.flush();
            wr.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }


    // Reads a response for a given connection and returns it as a string.
    private static String readResponse(HttpsURLConnection connection) throws IOException {
        StringBuilder str = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection
                .getInputStream()));
        String line = "";
        while ((line = br.readLine()) != null) {
            str.append(line + System.getProperty("line.separator"));
        }
        return str.toString().trim();
    }


}
