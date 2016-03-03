package com.example.iserbai.moviedb;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*@Override
    public void onConfigurationChanged (Configuration config) {
        super.onConfigurationChanged(config);

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v(LOG_TAG, "Orientation - Landscape");
            setContentView(R.layout.activity_main_landscape);

            Toast.makeText(this, "Orientation - Landscape", Toast.LENGTH_SHORT).show();

        } else {
            Log.v(LOG_TAG, "Orientation - Portrait");
            setContentView(R.layout.activity_main_portrait);

            Toast.makeText(this, "Orientation - Portrait", Toast.LENGTH_SHORT).show();
        }
    }*/
}
