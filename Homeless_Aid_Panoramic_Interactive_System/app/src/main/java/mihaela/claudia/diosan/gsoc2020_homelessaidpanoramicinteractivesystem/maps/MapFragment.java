package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.maps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.donor.HelpFragment;

import static android.content.Context.MODE_PRIVATE;

public class MapFragment extends Fragment implements OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore mFirestore;
    /*SharedPreferences*/
    private SharedPreferences homelessPref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
      //  String sPref = preferences.getString("networkPreference", "Todas");

        homelessPref = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);
      //  String status = getConnectivityStatusString(getContext());

      //  if (sPref.equals("Wi-Fi") && status.equals(getString(R.string.mobile_connected)) || status.equals(getString(R.string.no_network_operating))){
          //  snackBarWifi();
      //  }else{
            firebaseInit();
            // Get the map and register for the ready callback
            SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
            mMapFragment.getMapAsync(this);

      //  }



        return view;
    }

/*    private void snackBarWifi(){
        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.map_snackbar), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.snackbar_option), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }*/

 /*   static String getConnectivityStatusString(Context context) {
        String status;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = context.getResources().getString(R.string.wifi_connected);
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = context.getResources().getString(R.string.mobile_connected);
                return status;
            }
        } else {
            status = context.getResources().getString(R.string.no_network_operating);
            return status;
        }
        return "";
    }*/

    private void firebaseInit() {
        mFirestore = FirebaseFirestore.getInstance();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(true);

        addMarkersToMap();

        mFirestore.collection("homeless")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (task.isSuccessful()){
                                    String latitude = document.getString("homelessLatitude");
                                    String longitude = document.getString("homelessLongitude");

                                    Double double_lat = Double.parseDouble(latitude);
                                    Double double_long = Double.parseDouble(longitude);


                                    final LatLng position = new LatLng(aroundUp(double_lat, 5), aroundUp(double_long, 5));

                                    LatLngBounds bounds = new LatLngBounds.Builder()
                                            .include(position)
                                            .build();
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                                }

                            }
                        }
                    }
                });
    }


    private static double aroundUp(double number, int canDecimal) {
        int cifras = (int) Math.pow(10, canDecimal);
        return Math.ceil(number * cifras) / cifras;
    }

    private void addMarkersToMap() {
        //Colored icon
        mFirestore.collection("homeless")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String latitude = document.getString("homelessLatitude");
                                String longitude = document.getString("homelessLongitude");
                                String username = document.getString("homelessUsername");
                                String schedule = document.getString("homelessSchedule");
                                String image = document.getString("image");


                                final LatLng position = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                                mMap.addMarker(new MarkerOptions()
                                        .position(position)
                                        .title(username)
                                        .snippet(getString(R.string.here) + " " + schedule)
                                        .icon(vectorToBitmap(R.drawable.ic_person_pin_circle_black_24dp, Color.parseColor("#F10000"))));

                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {

                                        SharedPreferences.Editor editor = homelessPref.edit();
                                        editor.putString("homelessUsername",  marker.getTitle()).apply();

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.donor_fragment_container, new HelpFragment())
                                               .addToBackStack(null).commit();
                                    }
                                });

                            }
                        }
                    }
                });


    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}

