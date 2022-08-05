package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

import static android.content.Context.MODE_PRIVATE;


public class EditHomelessFragment extends Fragment implements View.OnClickListener {

    /*Storage Permission*/
    private static  final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Integer SELECT_FILE = 0;

    /*ImageView*/
    private ImageView editProfileImage;
    private Uri selectedImagePath;

    /*TextView*/
    private TextView usernameTV;
    private TextView location;
    private TextView need;

    /*EditTexts*/
    private TextInputEditText phoneET;
    private TextInputEditText lifeHistoryET;
    private TextInputEditText scheduleET;

    /*Shared Preferences*/
    private SharedPreferences preferences;
    private String username;

    /*Views*/
    private View view;

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private StorageReference storageReference;

    /*Buttons*/
    private MaterialButton cancelBtn;
    private MaterialButton saveBtn;
    private MaterialButton deleteBtn;
    private MaterialButton changeProfilePhoto;

    private ChipGroup chipGroup;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    private Geocoder mGeocoder;
    private Map<String,String> cities = new HashMap<>();

    /*Autocomplete place field*/
    private List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_homeless, container, false);

        initViews();
        firebaseInit();
        initPlaces();
        updateLocation();

        username = preferences.getString("homelessUsername", "");
        usernameTV.setText(username);

        getCurrentInfo(username);
        mGeocoder = new Geocoder(getActivity(), Locale.getDefault());

        return view;
    }

    private void firebaseInit(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void initViews(){
        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);
        usernameTV = view.findViewById(R.id.homeless_username_tv);
        phoneET = view.findViewById(R.id.homeless_edit_phone);
        lifeHistoryET = view.findViewById(R.id.homeless_edit_life_history);
        scheduleET = view.findViewById(R.id.homeless_edit_schedule);
        location = view.findViewById(R.id.selected_location_text);
        need = view.findViewById(R.id.most_important_need);
        editProfileImage = view.findViewById(R.id.edit_profile_image);
        changeProfilePhoto = view.findViewById(R.id.edit_homeless_profile_photo_button);
        cancelBtn = view.findViewById(R.id.cancelEditButton);
        saveBtn = view.findViewById(R.id.saveHomelessBtn);
        deleteBtn = view.findViewById(R.id.deleteProfileBtn);
        chipGroup = (ChipGroup) view.findViewById(R.id.chip_group);
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_edit);

    }

    private void initPlaces(){
        Places.initialize(view.getContext(), getString(R.string.API_KEY));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        changeProfilePhoto.setOnClickListener(this);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {
                // Handle the checked chip change.
                Chip chip = chipGroup.findViewById(checkedId);
                if(chip != null){
                    need.setText(chip.getText().toString());
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelEditButton:
                startActivity(new Intent(getActivity(),HomeVolunteer.class));
                break;
            case R.id.saveHomelessBtn:
                if (isLifeHistoryValid(lifeHistoryET.getText().toString()) && isValidPhoneNumber(phoneET.getText().toString())){
                    updateProfileInfo(username);
                    MainActivity.showSuccessToast(getActivity(),getString(R.string.update_success_toast));
                    startActivity(new Intent(getActivity(), HomeVolunteer.class));
                }
                break;
            case R.id.deleteProfileBtn:
                showDeleteDialog();
                break;
            case R.id.edit_homeless_profile_photo_button:
                verifyStoragePermissions(getActivity());
                chooseImage();
                break;
        }
    }


    private void showDeleteDialog(){
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(getString(R.string.delete_dialog_title))
                .setMessage(getString(R.string.delete_message))
                .setIcon(R.drawable.delete_homeless_icon)
                .setPositiveButton(getString(R.string.delete_confirm_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHomeless(username);
                        deleteProfilePhoto(username);
                        startActivity(new Intent(getActivity(), HomeVolunteer.class));
                    }
                })
                .setNegativeButton(getString(R.string.delete_cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void deleteHomeless(String username){
        mFirestore.collection("homeless").document(username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //  Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        MainActivity.showSuccessToast(getActivity(),getString(R.string.delete_correct_toast));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //   Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void deleteProfilePhoto(String username){
        storageReference.child("homelessProfilePhotos/" + user.getEmail() + ":" + username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void uploadPhotoToFirebase(final Uri selectedImagePath){

        if (selectedImagePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.uploading_photo));
            progressDialog.show();

            final DocumentReference documentReference = mFirestore.collection("homeless").document(username);
            final StorageReference ref = storageReference.child("homelessProfilePhotos/" + user.getEmail() + "_" + username);

            ref.putFile(selectedImagePath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    documentReference.update("image", uri.toString());


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            String error = e.getMessage();
                            Toast.makeText(getActivity(), "Failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(getString(R.string.uploaded_photo) + " " + (int) progress + "%");
                }
            });

        }

    }


    private void getCurrentInfo(String username){
        DocumentReference documentReference = mFirestore.collection("homeless").document(username);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null){

                        String phone = documentSnapshot.getString("homelessPhoneNumber");
                        String lifeHistory = documentSnapshot.getString("homelessLifeHistory");
                        String schedule = documentSnapshot.getString("homelessSchedule");
                        String locationValue = documentSnapshot.getString("homelessAddress");
                        String needValue = documentSnapshot.getString("homelessNeed");
                        String image = documentSnapshot.getString("image");


                        phoneET.setHint(phone);
                        lifeHistoryET.setHint(lifeHistory);
                        scheduleET.setHint(schedule);
                        location.setText(locationValue);
                        need.setText(needValue);
                        autocompleteSupportFragment.setHint(locationValue);


                        Glide
                                .with(getActivity())
                                .load(image)
                                .placeholder(R.drawable.no_profile_image)
                                .into(editProfileImage);


                    }
                }
            }
        });

    }


    private void updateProfileInfo(String username){

        uploadPhotoToFirebase(selectedImagePath);

        DocumentReference documentReference = mFirestore.collection("homeless").document(username);

        String phoneNumber = phoneET.getText().toString();
        String schedule = scheduleET.getText().toString();
        String lifeHistory = lifeHistoryET.getText().toString();

        if (!phoneNumber.isEmpty()){
            documentReference.update("homelessPhoneNumber", phoneNumber);
        }

        if (!schedule.isEmpty()){
            documentReference.update("homelessSchedule", schedule);
        }


        if (!lifeHistory.isEmpty()){
            documentReference.update("homelessLifeHistory", lifeHistory);
        }

        documentReference.update("homelessNeed", need.getText().toString());
    }

    private void updateLocation(){
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {

                if (place.getLatLng() != null) {
                    double latitude = aroundUp(place.getLatLng().latitude,5);
                    double longitude = aroundUp(place.getLatLng().longitude,5) ;


                    String homelessAddress = place.getAddress();
                    String homelessLatitude = Double.toString(latitude);
                    String homelessLongitude = Double.toString(longitude);
                    String city = getCityNameByCoordinates(latitude, longitude);
                    String country = getCountryNameByCoordinates(latitude, longitude);

                    cities.put("city", city);
                    cities.put("country", country);

                    DocumentReference documentReference = mFirestore.collection("homeless").document(username);
                    mFirestore.collection("cities").document(city).set(cities, SetOptions.merge());

                    location.setText(homelessAddress);
                    documentReference.update("homelessAddress", homelessAddress);
                    documentReference.update("homelessLongitude", homelessLongitude);
                    documentReference.update("homelessLatitude", homelessLatitude);


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

    private boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() == 0){
            return true;
        }else if (target.length() < 6 || target.length() > 13) {
            phoneET.setError(getString(R.string.phone_error_text));
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    private boolean isLifeHistoryValid(CharSequence lifeHistory){
        if (lifeHistory.length() != 0){
            if (lifeHistory.length() > 19 && lifeHistory.length()<=400) {
                return true;
            }else{
                lifeHistoryET.setError(getString(R.string.life_hitory_error));

            }
            return false;
        }
        return true;
    }


    private void chooseImage(){
        Intent selectFileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectFileIntent.setType("image/*");
        startActivityForResult(selectFileIntent.createChooser(selectFileIntent, getString(R.string.dialog_select_file)), SELECT_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //  super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK && null != data){

            selectedImagePath = data.getData();

            Glide
                    .with(getActivity())
                    .load(selectedImagePath)
                    .placeholder(R.drawable.no_profile_image)
                    .into(editProfileImage);

        }
    }





    private void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}

