package com.galaxy.orbitsatellitevisualizer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionController;

public class RocketsActivity extends TopBarActivity {
    private static final String TAG_DEBUG = "RocketActivity";
    public static final String EXTRA_MESSAGE = "com.galaxy.orbitsatellitevisualizer.MESSAGE";
    private Dialog dialog;
    private Button buttRocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rockets);
        View topBar = findViewById(R.id.top_bar);
        buttRocket = topBar.findViewById(R.id.butt_rockets);
    }

    public void cleanLG(View view){
        ActionController.getInstance().cleanFileKMLs(0);
    }

    public void sendRocketTrajectory(View view) {
        ActionController.getInstance().sendRocketTraj(RocketsActivity.this);
    }
}