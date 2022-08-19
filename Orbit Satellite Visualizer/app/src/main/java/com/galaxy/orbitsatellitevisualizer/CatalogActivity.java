package com.galaxy.orbitsatellitevisualizer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.galaxy.orbitsatellitevisualizer.create.utility.connection.LGConnectionTest;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.Action;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionController;
import com.galaxy.orbitsatellitevisualizer.dialog.CustomDialogUtility;
import com.galaxy.orbitsatellitevisualizer.utility.ConstantPrefs;

import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


import static java.lang.Thread.sleep;

public class CatalogActivity extends TopBarActivity {
    private static final String TAG_DEBUG = "CatalogActivity";
    public static final String EXTRA_MESSAGE = "com.galaxy.orbitsatellitevisualizer.MESSAGE";
    private Dialog dialog;
    private Button button9;
    private Button buttCatalog;
    private Handler mHandler = new Handler();
    private String scn;
    Timer t = new Timer();

    private Handler handler = new Handler();
    private TextView connectionStatus;
    private Button buttDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        View topBar = findViewById(R.id.top_bar);
        buttCatalog = topBar.findViewById(R.id.butt_scn);
        buttDemo = topBar.findViewById(R.id.butt_demo);
        connectionStatus = findViewById(R.id.connection_status);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);
    }

    /* FUNCIONAL, SENSE PERIODIC REPEAT */
    public void sendMessage(View view) {
        ActionController.getInstance().cleanFileKMLs(0);
        Intent intent = new Intent(this, CatalogActivity.class);
        EditText editText = (EditText) findViewById(R.id.scn_field);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        System.out.println("sendMessage output: " + message);

        handler.postDelayed(() -> {
            ActionController.getInstance().sendLiveSCN(CatalogActivity.this, message);
        }, 1000);
    }

    /**
     * Set the connection status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
        } else {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
        }
    }

    public void load_enxaneta(View v){
        String scn = "47954";
        TextView t = (TextView) findViewById(R.id.scn_field);
        t.setText(scn);
    }

    public void load_iss(View v){
        String scn = "25544";
        TextView t = (TextView) findViewById(R.id.scn_field);
        t.setText(scn);
    }

    public void load_starlink(View v){
        String scn = "44238";
        TextView t = (TextView) findViewById(R.id.scn_field);
        t.setText(scn);
    }

    public void load_iridium(View v){
        String scn = "24793";
        TextView t = (TextView) findViewById(R.id.scn_field);
        t.setText(scn);
    }

    public void load_tiangong(View v){
        String scn = "25544";
        TextView t = (TextView) findViewById(R.id.scn_field);
        t.setText(scn);
    }

    public void cleanLG(View view){
        ActionController.getInstance().cleanFileKMLs(0);
    }
}