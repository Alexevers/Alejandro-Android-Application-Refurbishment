package com.galaxy.orbitsatellitevisualizer;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.galaxy.orbitsatellitevisualizer.TopBarActivity;
import com.galaxy.orbitsatellitevisualizer.R;

public class AboutActivity extends TopBarActivity {

    private Button buttAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View topBar = findViewById(R.id.top_bar);
        buttAbout = topBar.findViewById(R.id.butt_about);

        TextView linkLinkedId= findViewById(R.id.linked_id);
        linkLinkedId.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkGithub= findViewById(R.id.github_account);
        linkGithub.setMovementMethod(LinkMovementMethod.getInstance());
    }
}