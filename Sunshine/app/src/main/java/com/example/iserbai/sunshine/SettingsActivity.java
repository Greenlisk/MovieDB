package com.example.iserbai.sunshine;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by iserbai on 12.02.16.
 */
public class SettingsActivity extends PreferenceActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
        
    }


}
