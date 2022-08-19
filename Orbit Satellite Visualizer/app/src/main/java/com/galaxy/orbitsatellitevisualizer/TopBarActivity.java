package com.galaxy.orbitsatellitevisualizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TopBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bar);
    }


    /**
     * Pass form the actual activity to the activity Main Activity (connect)
     *
     * @param view The view which is call.
     */
    public void buttConnectMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Pass form the actual activity to the activity Demo
     *
     * @param view The view which is call.
     */
    public void buttSingleSpacecraft(View view) {
        Intent intent = new Intent(getApplicationContext(), SingleSpacecraftsActivity.class);
        startActivity(intent);
    }

    /**
     * Pass form the actual activity to the activity About
     *
     * @param view The view which is call.
     */
    public void buttAbout(View view) {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);
    }

    public void buttConstellations(View view) {
        Intent intent = new Intent(getApplicationContext(), ConstellationsActivity.class);
        startActivity(intent);
    }

    public void buttCatalog(View view) {
        Intent intent = new Intent(getApplicationContext(), CatalogActivity.class);
        startActivity(intent);
    }

    public void buttRockets(View view) {
        Intent intent = new Intent(getApplicationContext(), RocketsActivity.class);
        startActivity(intent);
    }

    public void buttSpaceports(View view) {
        Intent intent = new Intent(getApplicationContext(), SpaceportsActivity.class);
        startActivity(intent);
    }

}