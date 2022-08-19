package com.galaxy.orbitsatellitevisualizer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.galaxy.orbitsatellitevisualizer.create.utility.model.Action;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionController;
import com.galaxy.orbitsatellitevisualizer.dialog.CustomDialogUtility;
import com.galaxy.orbitsatellitevisualizer.utility.ConstantPrefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SingleSpacecraftsActivity extends TopBarActivity {

    private static final String TAG_DEBUG = "SingleSpacecraftsActivity";

    private Dialog dialog;
    private Handler handler = new Handler();
    private TextView connectionStatus;
    private List<Action> actionsSaved = new ArrayList<>();
    private Button buttDemo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_spacecrafts);
        View topBar = findViewById(R.id.top_bar);
        buttDemo = topBar.findViewById(R.id.butt_spaceports);
        connectionStatus = findViewById(R.id.connection_status);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);

    }

    public void sendStarlink(View view) {
        ActionController.getInstance().sendStarlinkfile(SingleSpacecraftsActivity.this);
    }

    public void sendEnxaneta(View view) {
        ActionController.getInstance().sendEnxanetaFile(SingleSpacecraftsActivity.this);
    }

    public void sendISS(View view) {
        ActionController.getInstance().sendISSfile(SingleSpacecraftsActivity.this);
    }
}
