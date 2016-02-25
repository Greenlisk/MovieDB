package com.example.green.mypracticeapplication;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by green on 2/22/16.
 */
public class MainFragment extends Fragment {
    BroadcastReceiver broadcastReceiver;
    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                TextView text_react = (TextView)getView().findViewById(R.id.text_reaction);
                if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                    text_react.setText("Power Connected!!!");
                } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                    text_react.setText("Power DISConnected!!!");
                }
                Toast.makeText(context, "Something with Power!!!", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSavedInstanceState) {
        View rootVIew = inflater.inflate(R.layout.main_window, container, false);
        setHasOptionsMenu(true);
        return rootVIew;

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        TextView textView = (TextView)getView().findViewById(R.id.text_reaction);
        textView.setText("Action Button clicked!!!!");
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

}
