package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.maps.OnMapAndViewReadyListener;

import static android.content.Context.MODE_PRIVATE;

public class LocationFragment extends Fragment implements  OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, OnMapReadyCallback, View.OnClickListener {

    /*View*/
    private View view;

    /*Map and Autocomplete Place*/
    private List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    private GoogleMap mGoogleMap;


    /*TextViews*/
    private TextView selectedLocationTV;
    private TextView textLocation;

    /*Buttons*/
    private MaterialButton cancelBtn;
    private MaterialButton saveBtn;

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseUser user;
    private Map<String,String> homeless = new HashMap<>();
    private Map<String,String> cities = new HashMap<>();

    /*SharedPreferences*/
    private SharedPreferences preferences;

    private Geocoder mGeocoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);

        initViews(view);
        initMapAndPlaces();
        firebaseInit();
        setupPlaceAutoComplete();

        mGeocoder = new Geocoder(getActivity(), Locale.getDefault());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelLocationBtn:
                deleteExistingInfo();
                startActivity(new Intent(getActivity(),HomeVolunteer.class));
                break;
            case R.id.saveLocationButton:
                if (!selectedLocationTV.getText().toString().isEmpty()){
                    goToNeedsFragment();
                    MainActivity.showSuccessToast(getActivity(), getString(R.string.correct_saved_info));
                }else{
                    MainActivity.showErrorToast(getActivity(), getString(R.string.location_error_toast));
                }
                break;
        }
    }

    private void initViews(View view){
        selectedLocationTV = view.findViewById(R.id.selected_location_tv);
        textLocation = view.findViewById(R.id.selected_location_text);
        cancelBtn = view.findViewById(R.id.cancelLocationBtn);
        saveBtn = view.findViewById(R.id.saveLocationButton);

    }

    private void initMapAndPlaces(){
        Places.initialize(view.getContext(), getString(R.string.API_KEY));

        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void firebaseInit(){
        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();

    }

    public void setupPlaceAutoComplete(){
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setHint(getString(R.string.tv_location_hint));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {

                if (place.getLatLng() != null) {
                    textLocation.setVisibility(View.VISIBLE);
                    double latitude = aroundUp(place.getLatLng().latitude,5);
                    double longitude = aroundUp(place.getLatLng().longitude,5) ;
                    String name = place.getName();

                    String homelessUsername = preferences.getString("homelessUsername","");
                    String homelessAddress = place.getAddress();
                    String homelessLatitude = Double.toString(latitude);
                    String homelessLongitude = Double.toString(longitude);
                    String city = getCityNameByCoordinates(latitude, longitude);
                    String country = getCountryNameByCoordinates(latitude, longitude);

                    homeless.put("homelessAddress", homelessAddress);
                    homeless.put("homelessLongitude", homelessLongitude);
                    homeless.put("homelessLatitude", homelessLatitude);
                    homeless.put("city", city);
                    homeless.put("country", country);

                    cities.put("city", city);
                    cities.put("country", country);



                    mFirestore.collection("homeless").document(homelessUsername).set(homeless, SetOptions.merge());
                    mFirestore.collection("cities").document(city).set(cities,SetOptions.merge());

                    selectedLocationTV.setText(place.getAddress());
                    // Creating a marker
                    final MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(place.getLatLng());

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(name);

                    // Clears the previously touched position
                    mGoogleMap.clear();

                    mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            // Animating to the touched position
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13.3f));

                            // Placing a marker on the touched position
                            mGoogleMap.addMarker(markerOptions);
                        }
                    });

                }

            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(view.getContext(), ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCityNameByCoordinates(double lat, double lon)  {

        List<Address> addresses = null;
        try {
            addresses = mGeocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getLocality();
        }
        return null;
    }

    private String getCountryNameByCoordinates(double lat, double lon){
        List<Address> addresses = null;
        try {
            addresses = mGeocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0)
        {
            return addresses.get(0).getCountryName();
        }
        return null;
    }

    private static double aroundUp(double number, int canDecimal) {
        int cifras = (int) Math.pow(10, canDecimal);
        return Math.ceil(number * cifras) / cifras;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    private void deleteExistingInfo(){
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        String username = preferences.getString("homelessUsername", "");

        mFirestore.collection("homeless").document(username).delete();

        storageReference.child("homelessSignatures/" + firstName + " " + lastName).delete();

        storageReference.child("homelessProfilePhotos/" + user.getEmail() + "->" + username).delete();

    }

    private void goToNeedsFragment(){
        ViewPager viewPager = getActivity().findViewById(R.id.create_homeless_view_pager);

        int position = viewPager.getCurrentItem();

        position++;
        viewPager.setCurrentItem(position);

        //hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


}
