package com.example.iserbai.sunshine;

import android.content.ContentValues;
import android.support.v4.widget.CursorAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.content.Context;;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.iserbai.sunshine.data.WeatherContract;
import com.example.iserbai.sunshine.data.WeatherDbHelper;

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
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public ForecastFragment() {
    }

    private final static String LOG_TAG = "ForecastFragment";
    private final int LOADER_ID = 0;
    private ForecastListAdapter week;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        week = new ForecastListAdapter(getActivity(), null);
        getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);
        ListView list = (ListView) rootView.findViewById(R.id.forecast_list_view);
        list.setAdapter(week);
        setHasOptionsMenu(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String EXTRA_MESSAGE = "forecast_data";
                TextView textView = (TextView) view;
                CharSequence intentText = textView.getText();
                Intent intent = new Intent(getContext(), DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, intentText);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String postalCode = PreferenceManager.getDefaultSharedPreferences(this.getContext())
                .getString("location", "94043");
        String units = PreferenceManager.getDefaultSharedPreferences(this.getContext())
                .getString("units", "metric");
        getLoaderManager().restartLoader(LOADER_ID, null, this);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_refresh) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle onSavedInstanceState) {
        WeatherLoader weatherLoader = new WeatherLoader(getActivity());
        return weatherLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        week.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class ForecastListAdapter extends CursorAdapter {
        ForecastListAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView forecast = (TextView) view.findViewById(R.id.forecast_text_view);
            StringBuilder builder = new StringBuilder();
            builder.append(WeatherLoader.getReadableDateString(cursor.getLong(0)) + "-");
            builder.append(cursor.getString(1) + "-");
            builder.append(cursor.getString(2) + "-");
            builder.append(cursor.getString(3) + "-");
            forecast.setText(builder.toString());
        }
    }

    private static class WeatherLoader extends CursorLoader {
        private String postalCode;
        private String units;
        private Context context;
        WeatherDbHelper weatherDbHelper;

        WeatherLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public Cursor loadInBackground() {
            if (weatherDbHelper == null) {
                Log.v(LOG_TAG, "Creating DB!!!!");
                weatherDbHelper = new WeatherDbHelper(context);
            }
            postalCode = PreferenceManager.getDefaultSharedPreferences(this.getContext())
                    .getString("location", "94043");
            units = PreferenceManager.getDefaultSharedPreferences(this.getContext())
                    .getString("units", "metric");
            return getForecastCursor(postalCode, units);

        }

    }
}