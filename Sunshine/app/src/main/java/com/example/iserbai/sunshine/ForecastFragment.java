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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.iserbai.sunshine.data.WeatherContract;
import com.example.iserbai.sunshine.data.WeatherDbHelper;
import com.example.iserbai.sunshine.data.WeatherFetcher;

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
    private final static String LOG_TAG = "ForecastFragment";
    private final int LOADER_ID = 0;
    private ForecastListAdapter week;
    WeatherFetcher weatherFetcher;

    public ForecastFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        weatherFetcher = new WeatherFetcher(getContext());
            week = new ForecastListAdapter(getContext(), null);
        ListView list = (ListView) rootView.findViewById(R.id.forecast_list_view);
        list.setAdapter(week);
        setHasOptionsMenu(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String EXTRA_MESSAGE = "forecast_data";
                Intent intent = new Intent(getContext(), DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, position);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //getLoaderManager().restartLoader(LOADER_ID, null, this);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
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
        return new WeatherLoader(getContext(), weatherFetcher);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        week.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }

    private class ForecastListAdapter extends CursorAdapter {
        private final int VIEW_TYPE_TODAY = 0;
        private final int VIEW_TYPE_FUTURE = 1;
        ForecastListAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        private class ViewHolder {
            public final ImageView weatherIcon;
            public final TextView date;
            public final TextView weather;
            public final TextView tempMax;
            public final TextView tempMin;

            public ViewHolder(View view) {
                weatherIcon = (ImageView)view.findViewById(R.id.list_weather_image);
                date = (TextView) view.findViewById(R.id.list_date);
                weather = (TextView) view.findViewById(R.id.list_weather);
                tempMax = (TextView) view.findViewById(R.id.list_temperature_max);
                tempMin = (TextView) view.findViewById(R.id.list_temperature_min);
            }
        }

        @Override
        public int getViewTypeCount()
        {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            int layoutId;
            switch(getItemViewType(cursor.getPosition())) {
                case VIEW_TYPE_TODAY: layoutId = R.layout.list_item_today; break;
                default: layoutId = R.layout.list_item;
            }

            View rootView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            ViewHolder viewHolder = new ViewHolder(rootView);
            rootView.setTag(viewHolder);
            return rootView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();




            for (int i = 0; i < cursor.getColumnCount(); ++i) {
                Log.v(LOG_TAG, "Cursor: " + cursor.getLong(1) + " " + cursor.getString(2) + " " +
                        cursor.getString(3) + " " + cursor.getString(4));
            }

            viewHolder.weatherIcon.setImageResource(R.drawable.weather);
            viewHolder.date.setText(new SimpleDateFormat("EEE MMM dd").format(cursor.getLong(1) * 1000 + 1));
            viewHolder.weather.setText(cursor.getString(2));
            viewHolder.tempMax.setText(cursor.getString(4));
            viewHolder.tempMin.setText(cursor.getString(3));

        }
    }

    private static class WeatherLoader extends CursorLoader {

        WeatherFetcher weatherFetcher;

        WeatherLoader(Context context, WeatherFetcher weatherFetcher) {
            super(context);
            this.weatherFetcher = weatherFetcher;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor result = weatherFetcher.getWeekForecastCursor();
            Log.v(LOG_TAG, "Cursor size: " + result.getCount());
            return result;

        }

    }
}