package com.example.lab17_dev;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class FlightModeMonitor extends BroadcastReceiver {

    @Override
    public void onReceive(Context appContext, Intent incomingIntent) {
        // Vérification du type d'événement reçu
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(incomingIntent.getAction())) {
            
            // Extraction de l'état actuel du mode avion
            boolean isFlightActive = incomingIntent.getBooleanExtra("state", false);
            
            String statusMessage = isFlightActive 
                ? "✈️ Mode Avion ACTIVÉ - Communications coupées !" 
                : "📡 Mode Avion DÉSACTIVÉ - Réseaux rétablis";
            
            // Affichage d'une notification temporaire
            Toast.makeText(appContext, statusMessage, Toast.LENGTH_LONG).show();
        }
    }
}
