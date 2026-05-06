package com.example.lab17_dev;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StartupListener extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context appContext, Intent systemIntent) {
        // Vérification de l'action de démarrage complet
        if (Intent.ACTION_BOOT_COMPLETED.equals(systemIntent.getAction())) {
            Toast.makeText(appContext, "✅ Système démarré - Surveillance active !", Toast.LENGTH_LONG).show();
        }
    }
}
