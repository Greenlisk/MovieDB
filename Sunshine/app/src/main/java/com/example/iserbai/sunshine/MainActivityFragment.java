package com.example.iserbai.sunshine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] days={"Monday", "Tuesday", "Wednesday", "Thirsday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> week = new ArrayAdapter<String>(getActivity(), R.layout.forecast_text_view,
                R.id.forecast_text_view, Arrays.asList(days));
        ListView list = (ListView) rootView.findViewById(R.id.forecast_list_view);
        list.setAdapter(week);
        return rootView;
    }
}
