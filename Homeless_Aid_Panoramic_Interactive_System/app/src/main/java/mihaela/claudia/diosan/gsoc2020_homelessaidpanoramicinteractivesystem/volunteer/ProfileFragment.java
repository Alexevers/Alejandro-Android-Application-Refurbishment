package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    /*Storage Permissions*/
    private static  final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Integer SELECT_FILE = 0;

    /*ImageView*/
    private ImageView homelessProfileImage;
    private Uri selectedImagePath;

    /*Buttons*/
    private MaterialButton addProfilePhotoBtn;
    private MaterialButton cancelBtn;
    private MaterialButton saveBtn;

    private DatePickerDialog.OnDateSetListener setListener;

    /*Views*/
    private View view;

    /*EditTexts*/
    private TextInputEditText homelessUsername;
    private TextInputEditText homelessPhoneNumber;
    private TextInputEditText homelessBirthday;
    private TextInputEditText homelessLifeHistory;


    /*Firebase*/
    private StorageReference storageReference;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private Map<String,String> homeless = new HashMap<>();

    /*SharedPreferences*/
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);

        initViews();
        firebaseInit();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addProfilePhotoBtn.setOnClickListener(this);
        homelessBirthday.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_homeless_profile_photo_button:
                verifyStoragePermissions(getActivity());
                chooseImage();
                break;
            case R.id.homeless_birthday_editText:
                setTextDateListener();
                selectDate();
                break;
            case R.id.cancelProfileButton:
                deleteExistingInfo();
                startActivity(new Intent(getActivity(),HomeVolunteer.class));
                break;
            case R.id.saveProfileButton:
                if (isValidForm()){
                    checkUserExistsAndUploadData();
                }
                break;
        }
    }

    private void initViews(){
        homelessProfileImage = view.findViewById(R.id.homeless_profile_image);
        addProfilePhotoBtn = view.findViewById(R.id.add_homeless_profile_photo_button);

        homelessUsername = view.findViewById(R.id.homeless_username_editText);
        homelessPhoneNumber = view.findViewById(R.id.homeless_phone_number_editText);
        homelessBirthday = view.findViewById(R.id.homeless_birthday_editText);
        homelessLifeHistory = view.findViewById(R.id.homeless_life_history_editText);

        cancelBtn = view.findViewById(R.id.cancelProfileButton);
        saveBtn = view.findViewById(R.id.saveProfileButton);

    }

    private void firebaseInit(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();

    }

    private void uploadPhotoToFirebase(final Uri selectedImagePath){
        if (selectedImagePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.uploading_photo));
            progressDialog.show();

            final StorageReference ref = storageReference.child("homelessProfilePhotos/" + user.getEmail() + "_" + homelessUsername.getText().toString());
            ref.putFile(selectedImagePath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    homeless.put("image", uri.toString());

                                    mFirestore.collection("homeless").document(homelessUsername.getText().toString()).set(homeless, SetOptions.merge());

                                }
                            });
                            MainActivity.showSuccessToast(getActivity(),  getString(R.string.correct_saved_info));
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

    private void uploadDataToFirebase(){

        String homelessUsernameValue = homelessUsername.getText().toString();
        String homelessPhoneNumberValue = homelessPhoneNumber.getText().toString();
        String homelessBirthdayValue = homelessBirthday.getText().toString();
        String homelessLifeHistoryValue = homelessLifeHistory.getText().toString();

        homeless.put("homelessUsername", homelessUsernameValue);
        homeless.put("homelessBirthday", homelessBirthdayValue);
        homeless.put("homelessLifeHistory", homelessLifeHistoryValue);
        homeless.put("homelessPhoneNumber", homelessPhoneNumberValue);
        homeless.put("volunteerEmail", user.getEmail());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("homelessUsername", homelessUsernameValue).apply();

        if (homelessUsernameValue.isEmpty()){
            homelessUsername.setError(getString(R.string.empty_field_error));
        }else{
            mFirestore.collection("homeless").document(homelessUsername.getText().toString()).set(homeless)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            MainActivity.showErrorToast(getActivity(),"Error " + error);

                        }
                    });
        }

    }

    private void checkUserExistsAndUploadData(){
        mFirestore.collection("homeless").document(homelessUsername.getText().toString()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                MainActivity.showErrorToast(getActivity(), getString(R.string.user_exists));
                            }else{
                                uploadPhotoToFirebase(selectedImagePath);
                                uploadDataToFirebase();
                                goToLocationFragment();
                                MainActivity.showSuccessToast(getActivity(),  getString(R.string.correct_saved_info));
                            }
                        }
                    }
                });
    }

    private void deleteExistingInfo(){
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");

        storageReference.child("homelessSignatures/" + firstName + " " + lastName).delete();

    }

    private void selectDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), setListener, year, month, day);
        datePickerDialog.show();

    }


    private void setTextDateListener(){
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                homelessBirthday.setText(date);
            }
        };
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
            homelessProfileImage.setImageURI(selectedImagePath);

        }
    }

    private void goToLocationFragment(){
        ViewPager viewPager = getActivity().findViewById(R.id.create_homeless_view_pager);


        int position = viewPager.getCurrentItem();

        position++;
        viewPager.setCurrentItem(position);

        //hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    private void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private boolean isValidForm(){
        if (!AuxiliaryMethods.isValidPhoneNumber(homelessPhoneNumber.getText().toString())){
            homelessPhoneNumber.setError(getString(R.string.phone_error_text));
        }else if (!AuxiliaryMethods.isUsernameValid(homelessUsername.getText().toString())){
            homelessUsername.setError(getString(R.string.username_error_text));
        }else if (!AuxiliaryMethods.isLifeHistoryValid(homelessLifeHistory.getText().toString())){
            homelessLifeHistory.setError(getString(R.string.life_hitory_error));
        }
        return AuxiliaryMethods.isUsernameValid(homelessUsername.getText().toString()) && AuxiliaryMethods.isValidPhoneNumber(homelessPhoneNumber.getText().toString()) && AuxiliaryMethods.isLifeHistoryValid(homelessLifeHistory.getText().toString());
    }

}
