package com.galaxy.orbitsatellitevisualizer.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.galaxy.orbitsatellitevisualizer.R;

import java.util.Objects;

/**
 * Create the toast for the message to the user
 */
public class CustomDialog extends DialogFragment {

   //private final static String TAG_DEBUG = "DEBUGG_TOAST";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_fragment, null);

        v.getBackground().setAlpha(220);

        TextView textMessage = v.findViewById(R.id.message);

        Bundle bundle = getArguments();
        String text = Objects.requireNonNull(bundle).getString("TEXT","");
        textMessage.setText(text);


        Button ok = v.findViewById(R.id.ok);
        ok.setOnClickListener(view -> dismiss());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(v);
        return builder.create();
    }

}
