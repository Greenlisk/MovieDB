package com.example.iserbai.moviedb;

import android.net.Uri;
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
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by green on 3/1/16.
 */
public class MoviesExtractor {
    private static final String LOG_TAG = "MoviesExtractor";
    public final static short POPULAR = 1;
    public final static short RATED = 2;
    private short sortOrder;
    private int numPages = 0;
    private static String apiKey = "9a4a1c225f4326eb1a67cd049d405577";

    public ArrayList<Movie> moviesList;


    MoviesExtractor(short sortOrder){
        moviesList = new ArrayList<>(50);
        this.sortOrder = sortOrder;
    }

    public void clear() {
        moviesList.clear();
    }

    public void setSortOrder(short order) {
        sortOrder = order;
    }

    public int getNumPages() {
        return numPages;
    }

    public static Movie getMovie(long id) {
        Movie movie;
        try {
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http");
            uriBuilder.authority("api.themoviedb.org");
            uriBuilder.appendPath("3")
                    .appendPath("movie")
                    .appendPath(String.valueOf(id));

            uriBuilder.appendQueryParameter("api_key", apiKey);

            Uri uri = uriBuilder.build();
            URL url = new URL(uri.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            Log.v(LOG_TAG, uriBuilder.build().toString());

            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null) {
                Log.v(LOG_TAG, "NoURL connect!!!!");
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                Log.v(LOG_TAG, "Empty buffer!!!");
                return null;
            }
            Log.v(LOG_TAG, buffer.toString());
            JSONObject jsonObject = new JSONObject(buffer.toString());
            movie = new Movie(jsonObject);
            return movie;

        } catch (JSONException jsonex) {

        } catch (IOException ioex) {

        }
        return null;
    }

    public boolean getPage(int pageNumber) {
        try {
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http");
            uriBuilder.authority("api.themoviedb.org");
            uriBuilder.appendPath("3")
                    .appendPath("movie");
            if (sortOrder == POPULAR) {
                uriBuilder.appendPath("popular");
            } else {
                uriBuilder.appendPath("top_rated");
            }
            uriBuilder.appendQueryParameter("api_key", apiKey);

            Uri uri = uriBuilder.build();
            URL url = new URL(uri.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            Log.v(LOG_TAG, uriBuilder.build().toString());

            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null) {
                Log.v(LOG_TAG, "NoURL connect!!!!");
                return false;
            }
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                Log.v(LOG_TAG, "Empty buffer!!!");
                return false;
            }
            Log.v(LOG_TAG, buffer.toString());

            parseJSON(buffer.toString());

        } catch (IOException ioex) {
            Log.e(LOG_TAG, "IOException happened: " + ioex.getMessage());
            return false;
        }
        numPages++;
        return true;
    }

    private void parseJSON(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); ++i) {
                Movie movie = new Movie(array.getJSONObject(i));
                moviesList.add(movie);
                Log.v(LOG_TAG, movie.toString());
            }
        } catch (JSONException jsonex) {
            Log.e(LOG_TAG, "Unable to parse JSON!!!");
        }
    }
}
