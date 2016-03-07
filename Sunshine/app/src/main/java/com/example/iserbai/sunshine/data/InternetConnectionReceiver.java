package com.example.iserbai.sunshine.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by iserbai on 07.03.16.
 */
public class InternetConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent action) {
        String intent_action = action.getAction();
        if (intent_action == "android.net.conn.CONNECTIVITY_CHANGE") {
            Toast.makeText(context, "Connectivity Change", Toast.LENGTH_SHORT).show();
        }
    }
}
