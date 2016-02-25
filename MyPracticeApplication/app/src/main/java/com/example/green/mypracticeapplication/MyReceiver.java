package com.example.green.mypracticeapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

/**
 * Created by green on 2/24/16.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Something with Power!", Toast.LENGTH_LONG).show();
    }
}
