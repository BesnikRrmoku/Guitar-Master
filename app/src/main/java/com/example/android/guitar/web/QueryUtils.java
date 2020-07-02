package com.example.android.guitar.web;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static final String LOG_TAG = WebActivity.class.getName();

    private QueryUtils() {
    }
    private static List<web> extractFeatureFromJson(String guitarJSON) {
        if (TextUtils.isEmpty(guitarJSON)) {
            return null;
        }
        List<web> webs = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(guitarJSON);
            JSONArray guitarArray = baseJsonResponse.getJSONArray("features");
            for (int i = 0; i < guitarArray.length(); i++) {
                JSONObject currentGuitar = guitarArray.getJSONObject(i);
                JSONObject properties = currentGuitar.getJSONObject("properties");
                double rate = properties.getDouble("mag");
                String desc = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                web web = new web(rate, desc, time, url);
                webs.add(web);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing JSON results", e);
        }
        return webs;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<web> fetchGuitarData(String requestURL) {
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<web> webs = extractFeatureFromJson(jsonResponse);
        return webs;
    }

}
