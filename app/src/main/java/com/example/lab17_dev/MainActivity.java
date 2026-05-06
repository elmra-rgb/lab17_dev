package com.example.lab17_dev;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Déclaration des composants
    private FlightModeMonitor flightTracker;
    private CustomEventWatcher customWatcher;
    private boolean isFlightMonitorActive = false;
    private boolean isCustomWatcherActive = false;
    
    // Éléments d'interface
    private Button btnToggleFlightWatch, btnSendUserEvent, btnToggleCustomWatch;
    private TextView displayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des objets
        flightTracker = new FlightModeMonitor();
        customWatcher = new CustomEventWatcher();
        
        // Liaison des vues
        displayStatus = findViewById(R.id.displayStatus);
        btnToggleFlightWatch = findViewById(R.id.btnToggleFlightWatch);
        btnSendUserEvent = findViewById(R.id.btnSendUserEvent);
        btnToggleCustomWatch = findViewById(R.id.btnToggleCustomWatch);

        // Configuration des écouteurs de clics
        btnToggleFlightWatch.setOnClickListener(clickSource -> toggleFlightSurveillance());
        btnSendUserEvent.setOnClickListener(clickSource -> dispatchCustomSignal());
        btnToggleCustomWatch.setOnClickListener(clickSource -> toggleCustomSurveillance());
    }

    private void toggleFlightSurveillance() {
        if (!isFlightMonitorActive) {
            // Filtre pour capturer les changements de mode avion
            IntentFilter eventFilter = new IntentFilter();
            eventFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            
            // Activation du monitoring dynamique (avec flag pour Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(flightTracker, eventFilter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(flightTracker, eventFilter);
            }
            
            isFlightMonitorActive = true;
            displayStatus.setText("✈️ Surveillance Mode Avion : ACTIVE");
            btnToggleFlightWatch.setText("Désactiver surveillance avion");
            Toast.makeText(this, "Surveillance du mode avion activée", Toast.LENGTH_SHORT).show();
        } else {
            // Désactivation sécurisée
            unregisterReceiver(flightTracker);
            isFlightMonitorActive = false;
            displayStatus.setText("📴 Surveillance Mode Avion : INACTIVE");
            btnToggleFlightWatch.setText("Activer surveillance avion");
            Toast.makeText(this, "Surveillance du mode avion désactivée", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleCustomSurveillance() {
        if (!isCustomWatcherActive) {
            // Filtre pour notre événement personnalisé
            IntentFilter customFilter = new IntentFilter();
            customFilter.addAction("com.example.lab17_dev.USER_DEFINED_EVENT");
            
            // Activation du watcher personnalisé (avec flag pour Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(customWatcher, customFilter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(customWatcher, customFilter);
            }
            
            isCustomWatcherActive = true;
            btnToggleCustomWatch.setText("Désactiver watcher custom");
            Toast.makeText(this, "Watcher d'événements personnalisé activé", Toast.LENGTH_SHORT).show();
        } else {
            unregisterReceiver(customWatcher);
            isCustomWatcherActive = false;
            btnToggleCustomWatch.setText("Activer watcher custom");
            Toast.makeText(this, "Watcher d'événements désactivé", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchCustomSignal() {
        if (!isCustomWatcherActive) {
            Toast.makeText(this, "⚠️ Activez d'abord le watcher custom !", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Création et envoi du broadcast personnalisé
        Intent customIntent = new Intent("com.example.lab17_dev.USER_DEFINED_EVENT");
        customIntent.putExtra("custom_message", "Signal #" + System.currentTimeMillis());
        sendBroadcast(customIntent);
        
        Toast.makeText(this, "📤 Signal personnalisé transmis !", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        // Nettoyage obligatoire des receivers pour éviter les fuites mémoire
        if (isFlightMonitorActive) {
            unregisterReceiver(flightTracker);
        }
        if (isCustomWatcherActive) {
            unregisterReceiver(customWatcher);
        }
        super.onDestroy();
    }
}
