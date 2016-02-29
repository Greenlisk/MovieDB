package com.example.iserbai.moviedb;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by iserbai on 29.02.16.
 */
public class MainDescriptionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle onSave) {
        View rootView = inflater.inflate(R.layout.fragment_description, viewGroup);
        return rootView;
    }
}
