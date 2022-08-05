package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.connectivity.ConnectivityReceiver;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.register.RegisterUserActivity;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private BroadcastReceiver connectivityReceiver = null;
    Boolean connectivityOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityReceiver = new ConnectivityReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityReceiver.connectivityReceiverListener = (ConnectivityReceiver.ConnectivityReceiverListener) this;
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        ConnectivityReceiver.connectivityReceiverListener = (ConnectivityReceiver.ConnectivityReceiverListener) this;
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(String status) {

        if (status.equals(getResources().getString(R.string.wifi_connected))){
            //   Toast.makeText(getApplicationContext(), R.string.wifi_connected, Toast.LENGTH_SHORT).show();
        }else if (status.equals(getResources().getString(R.string.mobile_connected))){
            //  Toast.makeText(getApplicationContext(), R.string.mobile_connected, Toast.LENGTH_SHORT).show();
        }else {
            //   Toast.makeText(getApplicationContext(), R.string.no_network_operating, Toast.LENGTH_SHORT).show();
            createAlertDialog();
        }
    }


    public void createAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_network_operating))
                .setMessage(getString(R.string.option_network))
                .setIcon(R.drawable.no_internet_connexion_icon)
                .setPositiveButton(getString(R.string.positive_button_network), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        // System.exit(0);
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton(getString(R.string.negative_button_network), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showPositivePopup(Context context, String title, String message, String positiveBtn){
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showErrorToast(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view =toast.getView();
        view.setBackgroundColor(Color.TRANSPARENT);
        TextView toastMessage =  toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.RED);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setTextSize(15);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.error_drawable, 0,0,0);
        toastMessage.setPadding(10,10,10,10);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showSuccessToast(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view =toast.getView();
        view.setBackgroundColor(Color.WHITE);
        TextView toastMessage =  toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.BLUE);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setTextSize(15);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp,0,0,0);
        toastMessage.setPadding(10,10,10,10);
        toast.show();
    }

}
