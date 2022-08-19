package com.galaxy.orbitsatellitevisualizer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.orbitsatellitevisualizer.dialog.CustomDialogUtility;
import com.galaxy.orbitsatellitevisualizer.utility.ConstantPrefs;

import com.galaxy.orbitsatellitevisualizer.connection.LGCommand;
import com.galaxy.orbitsatellitevisualizer.connection.LGConnectionManager;
import com.galaxy.orbitsatellitevisualizer.connection.LGConnectionSendFile;
import com.galaxy.orbitsatellitevisualizer.create.utility.model.ActionController;
import com.galaxy.orbitsatellitevisualizer.TopBarActivity;


import java.util.regex.Pattern;

public class MainActivity extends TopBarActivity {

    private static final Pattern HOST_PORT = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$");

    private Button buttConnectMenu, buttConnectLiquidGalaxy, buttTryAgain;
    private TextView connecting, textUsername, textPassword, textInsertUrl;
    private EditText URI, username, password;
    private ImageView logo;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View topBar = findViewById(R.id.top_bar);
        buttConnectMenu = topBar.findViewById(R.id.butt_connect_menu);
        connecting = findViewById(R.id.connecting);

        buttConnectLiquidGalaxy = findViewById(R.id.butt_connect_liquid_galaxy);
        URI = findViewById(R.id.uri);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        textUsername = findViewById(R.id.text_username);
        textPassword = findViewById(R.id.text_password);
        textInsertUrl = findViewById(R.id.text_insert_url);
        logo = findViewById(R.id.logo);
        buttTryAgain = findViewById(R.id.butt_try_again);

        buttConnectLiquidGalaxy.setOnClickListener((view) -> connectionTest());
        buttTryAgain.setOnClickListener((view) -> {
            changeToMainView();
            SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
            editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
            editor.apply();
        });
    }


    @Override
    protected void onResume() {
        loadSharedData();
        super.onResume();
    }

    /**
     * Load the data that is in shared preferences
     */
    private void loadSharedData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);

        if(isConnected){
            changeToNewView();
        } else {
            String text = sharedPreferences.getString(ConstantPrefs.URI_TEXT.name(), "");
            String usernameText = sharedPreferences.getString(ConstantPrefs.USER_NAME.name(), "");
            String passwordText = sharedPreferences.getString(ConstantPrefs.USER_PASSWORD.name(), "");
            boolean isTryToReconnect = sharedPreferences.getBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), false);

            if(!text.equals("")) URI.setText(text);
            if(!usernameText.equals("")) username.setText(usernameText);
            if(!passwordText.equals("")) password.setText(passwordText);
            if(isTryToReconnect) buttConnectLiquidGalaxy.setText(getResources().getString(R.string.button_try_again));
        }
    }

    /**
     * Create a connection to the liquid galaxy and Test if it is working
     */
    private void connectionTest() {
        String hostPort = URI.getText().toString();
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        saveConnectionInfo(hostPort, usernameText, passwordText);

        if (!isValidHostNPort(hostPort)) {
            CustomDialogUtility.showDialog(MainActivity.this, getResources().getString(R.string.activity_connection_host_port_error));
            return;
        }

        Dialog dialog = CustomDialogUtility.getDialog(this, getResources().getString(R.string.connecting));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        changeButtonTextConnecting();
        createLgCommand(hostPort, usernameText, passwordText, dialog);
    }

    /**
     * Save the information in shared preference
     * @param hostPort Host:port
     * @param usernameText username
     * @param passwordText password
     */
    private void saveConnectionInfo(String hostPort, String usernameText, String passwordText) {
        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putString(ConstantPrefs.URI_TEXT.name(), hostPort);
        editor.putString(ConstantPrefs.USER_NAME.name(), usernameText);
        editor.putString(ConstantPrefs.USER_PASSWORD.name(), passwordText);
        editor.putBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), true);
        editor.apply();
    }

    /**
     * Create LgCommand  for the test
     * @param hostPort A string with the host and the port
     * @param usernameText The username
     * @param passwordText The password
     * @param dialog The dialog that is going to be shown to the user
     */
    private void createLgCommand(String hostPort, String usernameText, String passwordText, Dialog dialog) {
        final String command = "echo 'connection';";
        LGCommand lgCommand = new LGCommand(command, LGCommand.CRITICAL_MESSAGE, response -> dialog.dismiss());
        createConnection(usernameText, passwordText, hostPort, lgCommand);
        sendMessageError(lgCommand, dialog);
    }

    /**
     * Create the connection to the liquid galaxy
     * @param username The username of the connection
     * @param password The password of the connection
     * @param hostPort The String with the hos and the port of the liquid galxy
     * @param lgCommand The command to be send
     */
    private void createConnection(String username, String password, String hostPort, LGCommand lgCommand) {
        String[] hostNPort = hostPort.split(":");
        String hostname = hostNPort[0];
        int port = Integer.parseInt(hostNPort[1]);
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.setData(username, password, hostname, port);
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
        LGConnectionSendFile.getInstance().setData(username, password, hostname, port);
    }

    /**
     * Change the Button for a TextView with the text "Connecting ..."
     */
    private void changeButtonTextConnecting() {
        buttConnectLiquidGalaxy.setVisibility(View.INVISIBLE);
        connecting.setVisibility(View.VISIBLE);
        connecting.setText(getResources().getString(R.string.connecting));
    }


    /**
     * Create a Dialog to inform the user if the connection to the liquid galaxy has fail or not
     * @param lgCommand The command send to liquid galaxy
     * @param dialog The dialog that has been show to the user
     */
    private void sendMessageError(LGCommand lgCommand, Dialog dialog) {
        handler.postDelayed(() -> {
            if (dialog.isShowing()){
                LGConnectionManager.getInstance().removeCommandFromLG(lgCommand);
                dialog.dismiss();
                CustomDialogUtility.showDialog(MainActivity.this, getResources().getString(R.string.activity_connection_error_connect));
                SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
                editor.apply();
                connecting.setVisibility(View.INVISIBLE);
                buttConnectLiquidGalaxy.setVisibility(View.VISIBLE);
                buttConnectLiquidGalaxy.setText(getResources().getString(R.string.button_try_again));
            }else{
                CustomDialogUtility.showDialog(MainActivity.this, getResources().getString(R.string.activity_connection_success));
                SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), true);
                editor.apply();
                changeToNewView();
                ActionController.getInstance().sendBalloonWithLogos(MainActivity.this);
            }
        }, 2000);
    }

    /**
     * Change the view to the connected to liquid galaxy view
     */
    private void changeToNewView() {
        buttConnectLiquidGalaxy.setVisibility(View.INVISIBLE);
        connecting.setVisibility(View.INVISIBLE);
        URI.setVisibility(View.INVISIBLE);
        username.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        textUsername.setVisibility(View.INVISIBLE);
        textPassword.setVisibility(View.INVISIBLE);
        textInsertUrl.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
        buttTryAgain.setVisibility(View.VISIBLE);
    }

    /**
     * Change the view to the try to connect to liquid galaxy view
     */
    private void changeToMainView() {
        buttConnectLiquidGalaxy.setVisibility(View.VISIBLE);
        connecting.setVisibility(View.VISIBLE);
        URI.setVisibility(View.VISIBLE);
        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        textUsername.setVisibility(View.VISIBLE);
        textPassword.setVisibility(View.VISIBLE);
        textInsertUrl.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);
        buttTryAgain.setVisibility(View.INVISIBLE);
        loadSharedData();
    }

    /**
     * Validate if the string is valid
     *
     * @param hostPort The string with the host and the port
     * @return true if is a valid string with the host and the port
     */
    private boolean isValidHostNPort(String hostPort) {
        return HOST_PORT.matcher(hostPort).matches();
    }
}