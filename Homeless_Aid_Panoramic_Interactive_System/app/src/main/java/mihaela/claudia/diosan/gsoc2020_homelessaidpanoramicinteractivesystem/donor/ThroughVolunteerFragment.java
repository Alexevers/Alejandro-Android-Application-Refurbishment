package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.donor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

import static android.content.Context.MODE_PRIVATE;

public class ThroughVolunteerFragment extends Fragment implements View.OnClickListener {


    /*Views*/
    private View view;

    /*Autocomplete places*/
    private List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

    /*TextViews*/
    private TextView selectedDateDonor;
    private TextView selectedTimeDonor;
    private TextView locationDonor;

    /*Buttons*/
    private MaterialButton datePickerBtn;
    private MaterialButton timePickerBtn;
    private MaterialButton confirmBtn;

    private DatePickerDialog.OnDateSetListener setListener;

    /*Firebase*/
    private StorageReference storageReference;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;

    private Map<String,String> throughVolunteerDonations = new HashMap<>();
    private Map<String,Boolean> delivered = new HashMap<>();

    /*SharedPreferences*/
    private SharedPreferences preferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_through_volunteer, container, false);

        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);

        initViews(view);
        firebaseInit();

        initPlaces();
        setupPlaceAutoComplete();
        setTextDateListener();

        if (savedInstanceState != null){
            locationDonor.setText(savedInstanceState.getString("location"));
            selectedDateDonor.setText(savedInstanceState.getString("date"));
            selectedTimeDonor.setText(savedInstanceState.getString("time"));
        }

        return view;
    }

    private void initViews(View view) {
        locationDonor = view.findViewById(R.id.selected_location_donor);
        datePickerBtn = view.findViewById(R.id.date_picker_donor);
        timePickerBtn = view.findViewById(R.id.time_picker_donor);

        selectedDateDonor = view.findViewById(R.id.selected_date_donor);
        selectedTimeDonor = view.findViewById(R.id.selected_time_donor);

        confirmBtn = view.findViewById(R.id.donor_confirm_button);
    }

    private void firebaseInit(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datePickerBtn.setOnClickListener(this);
        timePickerBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_picker_donor:
                selectDate();
                break;
            case R.id.time_picker_donor:
                selectTime();
                break;
            case R.id.donor_confirm_button:
                if (isValidForm()){
                    uploadDataToFirebase();
                    MainActivity.showSuccessToast(getActivity(),getString(R.string.fr_tv_confirm_toast));
                    startActivity(new Intent(getContext(), HomeDonor.class));
                }
                break;
        }

    }

    private void uploadDataToFirebase(){

        String donorEmail = user.getEmail();
        String homelessUsername = preferences.getString("homelessUsername", "");
        String donationType = preferences.getString("donationType", "");

        addDonorData(donorEmail, homelessUsername, donationType);

        throughVolunteerDonations.put("donationLocation", locationDonor.getText().toString());
        throughVolunteerDonations.put("donationHour", selectedTimeDonor.getText().toString());
        throughVolunteerDonations.put("donationDate", selectedDateDonor.getText().toString());
        throughVolunteerDonations.put("donorEmail", donorEmail);
        delivered.put("delivered", false);

        mFirestore.collection("throughVolunteerDonations").document(donorEmail + "->" + homelessUsername + ":" + donationType).set(throughVolunteerDonations, SetOptions.merge());
        mFirestore.collection("throughVolunteerDonations").document(donorEmail + "->" + homelessUsername + ":" + donationType).set(delivered, SetOptions.merge());
    }

    private void addDonorData(final String donorEmail, final String homelessUsername,final String donationType){
        mFirestore.collection("donors").document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null){
                        String donorUsername = documentSnapshot.getString("donorUsername");
                        String donorPhone = documentSnapshot.getString("donorPhone");
                        throughVolunteerDonations.put("donorUsername",donorUsername);
                        throughVolunteerDonations.put("donorPhone", donorPhone);
                        mFirestore.collection("throughVolunteerDonations").document(donorEmail + "->" + homelessUsername + ":" + donationType).set(throughVolunteerDonations, SetOptions.merge());

                    }
                }
            }
        });
    }

    private boolean isValidForm(){
        if (selectedDateDonor.getText().toString().equals(getString(R.string.fr_tv_date))){
            MainActivity.showErrorToast( getActivity(), getString(R.string.date_error_toast));
            return false;
        }else if (selectedTimeDonor.getText().toString().equals(getString(R.string.fr_tv_hour))){
            MainActivity.showErrorToast( getActivity(),getString(R.string.time_error_toast));
            return false;
        }else if (locationDonor.getText().toString().equals(getString(R.string.fr_tv_location))){
            MainActivity.showErrorToast( getActivity(), getString(R.string.location_error_toast));
            return false;
        }
        return true;
    }


    private void initPlaces() {
        Places.initialize(view.getContext(), getString(R.string.API_KEY));

    }

    private void setupPlaceAutoComplete(){
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_donor);
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setHint(getString(R.string.choose_location_hint));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {

                if (place.getLatLng() != null) {

                    locationDonor.setText(place.getAddress());
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(view.getContext(), ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), setListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    private void setTextDateListener(){
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                selectedDateDonor.setText(date);
            }
        };
    }

    private void selectTime(){
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute + "h";
                selectedTimeDonor.setText(time);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString("location", locationDonor.getText().toString());
        outState.putString("date", selectedDateDonor.getText().toString());
        outState.putString("time", selectedTimeDonor.getText().toString());

        super.onSaveInstanceState(outState);
    }

}

