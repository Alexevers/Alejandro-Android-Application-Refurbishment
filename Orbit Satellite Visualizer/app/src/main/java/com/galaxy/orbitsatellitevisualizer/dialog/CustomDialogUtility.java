package com.galaxy.orbitsatellitevisualizer.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.galaxy.orbitsatellitevisualizer.R;

public class CustomDialogUtility {

    /**
     * It show a dialog
     * @param activity The activity where is call
     * @param message The message to pop up
     */
    public static void showDialog(AppCompatActivity activity, String message){
        CustomDialog dialogFragment = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", message);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(activity.getSupportFragmentManager(),"Image Dialog");
    }


    /**
     * It gives a dialog without Buttons
     * @param activity The activity where is call
     * @param message The message to pop up
     * @return The Dialog
     */
    public static Dialog getDialog(AppCompatActivity activity, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        @SuppressLint("InflateParams") View v = activity.getLayoutInflater().inflate(R.layout.dialog_fragment, null);
        v.getBackground().setAlpha(220);
        Button ok = v.findViewById(R.id.ok);
        ok.setVisibility(View.GONE);
        TextView textMessage = v.findViewById(R.id.message);
        textMessage.setText(message);
        textMessage.setTextSize(23);
        textMessage.setGravity(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(v);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


}
