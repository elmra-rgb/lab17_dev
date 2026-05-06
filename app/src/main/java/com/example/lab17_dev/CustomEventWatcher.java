package com.example.lab17_dev;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomEventWatcher extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context appContext, Intent eventIntent) {
        // Vérification de notre action personnalisée
        if ("com.example.lab17_dev.USER_DEFINED_EVENT".equals(eventIntent.getAction())) {
            String customData = eventIntent.getStringExtra("custom_message");
            Toast.makeText(appContext, "📨 Événement reçu : " + customData, Toast.LENGTH_LONG).show();
        }
    }
}
