package com.example.android.newsapp;

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

public class QueryUtils {
    private static final String LOG_TAG = MainActivity.class.getName();

    public static List<News> retrieveNews(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url);

        List<News> newsList = extractDataFromJson(jsonResponse);

        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {
        HttpURLConnection urlConnection = null;
        String jsonResponse = "";
        InputStream inputStream = null;

        if (url == null) {
            return jsonResponse;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("mainActivity", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) {
        InputStreamReader streamReader = null;
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;

        streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        bufferedReader = new BufferedReader(streamReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static List<News> extractDataFromJson(String jsonResponse) {

        List<News> newsList = new ArrayList<>();
        try {

            JSONObject json = new JSONObject(jsonResponse);
            JSONObject response = json.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject currentNews = results.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");
                String type = currentNews.getString("type");
                JSONArray tagsAuthor = currentNews.getJSONArray("tags");
                String author = "";
                if (tagsAuthor.length() != 0) {
                    JSONObject currentTagsAuthor = tagsAuthor.getJSONObject(0);
                    author = currentTagsAuthor.getString("webTitle");
                } else {
                    author = "No Author ..";
                }

                News newsObject = new News(title, date, url, type, section, author);
                newsList.add(newsObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}