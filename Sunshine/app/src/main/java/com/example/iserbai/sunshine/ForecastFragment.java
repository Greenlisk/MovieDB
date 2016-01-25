package com.example.iserbai.sunshine;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.Inflater;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] days={"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> week = new ArrayAdapter<String>(getActivity(), R.layout.forecast_text_view,
                R.id.forecast_text_view, Arrays.asList(days));
        ListView list = (ListView) rootView.findViewById(R.id.forecast_list_view);
        list.setAdapter(week);


        return rootView;
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
           new FetchWeatherTask().execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=32748294cda27141429efc3490f0e6d3");
        }
        return true;
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        public String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufReader = null;
            String resultJSON = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=32748294cda27141429efc3490f0e6d3");
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

                resultJSON = buffer.toString();
            } catch (IOException e) {
                Log.e("Fragment some exception", "Error mzfk",e);
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
            return resultJSON;
        }
    }
}

