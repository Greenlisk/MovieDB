package com.example.iserbai.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by iserbai on 12.02.16.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String LOG_TAG = "Settings_Fragment";
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        findPreference("location").setSummary(sharedPreferences.getString("location", ""));
        findPreference("units").setSummary(sharedPreferences.getString("units", ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(LOG_TAG, "Key: " + key);
        if (key.equals("location")) {
            Log.v(LOG_TAG, "Key: " + key);
            Preference locPref = findPreference(key);
            locPref.setSummary(sharedPreferences.getString(key, ""));
        }
        if (key.equals("units")) {
            Preference unitsPref = findPreference(key);
            unitsPref.setSummary(sharedPreferences.getString("units", ""));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
