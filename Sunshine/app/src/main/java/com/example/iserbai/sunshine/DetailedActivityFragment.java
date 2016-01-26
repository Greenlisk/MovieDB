package com.example.iserbai.sunshine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedActivityFragment extends Fragment {

    public DetailedActivityFragment() {
    }
    private final String LOG_TAG = "DetailedFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
        setHasOptionsMenu(true);

        Intent incomingIntent = this.getActivity().getIntent();
        String message = incomingIntent.getStringExtra("forecast_data");
        TextView textView = (TextView) rootView.findViewById(R.id.detailed_text);
        Log.v(LOG_TAG, message);
        textView.setText(message);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.detailed, menu);
    }/**/
}
