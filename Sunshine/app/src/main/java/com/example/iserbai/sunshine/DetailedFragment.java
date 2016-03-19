package com.example.iserbai.sunshine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.iserbai.sunshine.data.WeatherFetcher;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public DetailedFragment() {
    }
    private final String LOG_TAG = "DetailedFragment";
    private final int LOADER_ID = 3;
    TextView textView;
    int weekDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
        setHasOptionsMenu(true);
        Intent incomingIntent = this.getActivity().getIntent();
        weekDay = incomingIntent.getIntExtra("forecast_data", 0);
        textView = (TextView) rootView.findViewById(R.id.detailed_text);
        getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.detailed, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle onSavedInstanceState){
        return new DetailedLoader(getContext(), weekDay);

    }

    private static class DetailedLoader extends CursorLoader {
        Context context;
        int day;
        DetailedLoader(Context context, int day){
            super(context);
            this.day = day;
            this.context = context;
        }

        @Override
        public Cursor loadInBackground() {
            return new WeatherFetcher(context).getForecastCursor();
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        StringBuilder builder = new StringBuilder();
        cursor.moveToPosition(weekDay);
        for (int i = 0; i < cursor.getColumnCount(); ++i) {
            builder.append(cursor.getString(i) + "\n");
        }
        Log.v(LOG_TAG, "Text: " + builder.toString());
        textView.setText(builder.toString());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
