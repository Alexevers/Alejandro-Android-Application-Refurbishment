package com.galaxy.orbitsatellitevisualizer.create.utility.connection;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.galaxy.orbitsatellitevisualizer.connection.LGCommand;
import com.galaxy.orbitsatellitevisualizer.connection.LGConnectionManager;
import com.galaxy.orbitsatellitevisualizer.dialog.CustomDialogUtility;
import com.galaxy.orbitsatellitevisualizer.utility.ConstantPrefs;
import com.galaxy.orbitsatellitevisualizer.R;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class is in charge of testing and cleaning the liquid galaxy connection
 */
public class LGConnectionTest {

    private static Handler handler = new Handler();

    /**
     * Test the connection with the previous data connection
     * @param activity the activity calling the test
     * @param atomicBoolean the boolean to know if the connection is succesful or not
     */
    public static void testPriorConnection(AppCompatActivity activity, AtomicBoolean atomicBoolean) {
        Dialog dialog = CustomDialogUtility.getDialog(activity, activity.getResources().getString(R.string.test_connection));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LGCommand lgCommand = new LGCommand("echo 'connection';", LGCommand.CRITICAL_MESSAGE, response -> dialog.dismiss());
        sendCommand(lgCommand);
        handler.postDelayed(() -> {
            SharedPreferences.Editor editor = activity.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
            if (dialog.isShowing()) {
                LGConnectionManager.getInstance().removeCommandFromLG(lgCommand);
                dialog.dismiss();
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
                CustomDialogUtility.showDialog(activity, activity.getResources().getString(R.string.activity_connection_error));
                atomicBoolean.set(false);
            }else{
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), true);
                atomicBoolean.set(true);
            }
            editor.apply();
        }, 1000);
    }

    /**
     * Send a command to the liquid galaxy
     * @param lgCommand lg command to be send
     */
    private static void sendCommand(LGCommand lgCommand) {
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }
}
