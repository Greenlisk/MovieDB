package com.example.iserbai.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.iserbai.sunshine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by iserbai on 07.03.16.
 */
public class WeatherFetcher {
    private final String LOG_TAG = "WeatherFetcher";
    WeatherFetcher() {

    }

    public Cursor getForecastCursor(String... postcode) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufReader = null;
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected()))
        {
            return getLastWeatherData();
        }
        try {
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http");
            uriBuilder.authority("api.openweathermap.org");
            uriBuilder.appendPath("data");
            uriBuilder.appendPath("2.5");
            uriBuilder.appendPath("forecast");
            uriBuilder.appendPath("daily");
            uriBuilder.appendQueryParameter("id", postcode[0]);
            uriBuilder.appendQueryParameter("mode", "json");
            uriBuilder.appendQueryParameter("units", postcode[1]);
            uriBuilder.appendQueryParameter("APPID", "32748294cda27141429efc3490f0e6d3");
            Uri uri = uriBuilder.build();
            Log.v(LOG_TAG, uri.toString());
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();


            if (inputStream == null) {
                return null;
            }

            bufReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufReader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            Log.v(LOG_TAG, "Output: " + buffer.toString());
            try {

                return getWeatherDataFromJson(buffer.toString(), 7);
            } catch (JSONException je) {

            }

        } catch (IOException e) {
            Log.e("Fragment some exception", "Error mzfk", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }


    public static String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        SharedPreferences unitPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String units = unitPref.getString("units", "metric");
        if (units.equals(context.getResources().getString(R.string.pref_units_key_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!units.equals(context.getResources().getString(R.string.pref_units_key_metric))) {
            Log.d(LOG_TAG, "Unit type not found: " + units);
        }
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private Cursor getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_DATE = "dt";
        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WIND = "speed";
        final String OWM_DEGREES = "deg";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";
        final String OWM_ID = "id";
        final String OWM_CITY = "city";
        final String OWM_CITY_NAME = "name";
        final String OWM_COUNTRY = "country";
        final String OWM_COORD = "coord";
        final String OWM_LON = "lon";
        final String OWM_LAT = "lat";
        String cityExists;

        ContentValues cvCity = new ContentValues();
        SQLiteDatabase sqLiteDatabase = weatherDbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(WeatherContract.LocationEntry.TABLE_NAME,
                new String[]{WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{postalCode},
                null,
                null,
                null,
                "1");

        JSONObject jsonObject = new JSONObject(forecastJsonStr);

        if(cursor.moveToFirst()) {
            cityExists = cursor.getString(0);
            Log.v(LOG_TAG, "City exists at index: " + cityExists);
        } else {
            Log.v(LOG_TAG, "City doesn't exist");
            JSONObject city = jsonObject.getJSONObject(OWM_CITY);
            cvCity.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, postalCode);
            cvCity.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, city.getString(OWM_CITY_NAME));
            cvCity.put(WeatherContract.LocationEntry.COLUMN_COUNTRY_NAME, city.getString(OWM_COUNTRY));
            JSONObject loc = city.getJSONObject(OWM_COORD);
            cvCity.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, loc.getString(OWM_LAT));
            cvCity.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, loc.getString(OWM_LON));
            cityExists  = Long.toString(sqLiteDatabase.insert(WeatherContract.LocationEntry.TABLE_NAME, null, cvCity));
        }



        JSONArray weatherArray = jsonObject.getJSONArray(OWM_LIST);
        for(int i = 0; i < weatherArray.length(); ++i) {
            ContentValues cvWeather = new ContentValues();
            JSONObject listElement = (JSONObject)weatherArray.get(i);
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_DATE, listElement.getInt(OWM_DATE));

            JSONObject temp = listElement.getJSONObject(OWM_TEMPERATURE);
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, temp.getString(OWM_MAX));

            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, temp.getString(OWM_MIN));
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, listElement.getString(OWM_PRESSURE));
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, listElement.getString(OWM_HUMIDITY));
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, listElement.getString(OWM_WIND));
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, listElement.getString(OWM_DEGREES));

            JSONArray wArr = listElement.getJSONArray(OWM_WEATHER);
            JSONObject weather = (JSONObject)wArr.get(0);
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weather.getString(OWM_ID));
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, weather.getString(OWM_DESCRIPTION));
            cvWeather.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, cityExists);
            sqLiteDatabase.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, cvWeather);
        }

        Cursor checkCursor = sqLiteDatabase.rawQuery("select * from weather", null);
        if (checkCursor.moveToFirst()) {
            for (int j = 0; j < checkCursor.getCount(); ++j) {
                StringBuilder builder = new StringBuilder();
                for (int k = 0; k < checkCursor.getColumnCount(); ++k) {
                    builder.append(checkCursor.getString(k) + " ");
                }
                Log.v(LOG_TAG, builder.toString());
                checkCursor.moveToNext();
            }
        } else {
            Log.v(LOG_TAG, "No CUrsor");
        }
        return checkCursor;


    }

    private Cursor getLastWeatherData() {
        SQLiteDatabase sqLiteDatabase = weatherDbHelper.getWritableDatabase();
        Cursor checkCursor = sqLiteDatabase.rawQuery("select * from weather", null);
        if (checkCursor.moveToFirst()) {
            for (int j = 0; j < checkCursor.getCount(); ++j) {
                StringBuilder builder = new StringBuilder();
                for (int k = 0; k < checkCursor.getColumnCount(); ++k) {
                    builder.append(checkCursor.getString(k) + " ");
                }
                Log.v(LOG_TAG, builder.toString());
                checkCursor.moveToNext();
            }
        } else {
            Log.v(LOG_TAG, "No CUrsor");
        }
        return checkCursor;
    }
}
}


        }
