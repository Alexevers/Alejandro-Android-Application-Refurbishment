package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

import static android.content.Context.MODE_PRIVATE;


public class TermsFragment extends Fragment implements View.OnClickListener  {

    /*Storage permissions*/
    private static  final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private View view;

    /*Confirmation Dialog*/
    private Dialog confirmationDialog;
    private TextInputEditText homelessFirstName;
    private TextInputEditText homelessLastName;
    private MaterialButton cancelBtn;
    private MaterialButton confirmationBtn;

    /*Signature Pad*/
    private SignaturePad signaturePad;
    private MaterialButton mClearButton;
    private MaterialButton mSaveButton;

    /*Firebase*/
    private FirebaseUser user;
    private StorageReference storageReference;

    /*Shared Preference*/
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);
        view =  inflater.inflate(R.layout.fragment_terms, container, false);

        firebaseInit();
        initViews();

        return view;
    }


    private void firebaseInit(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void initViews(){
        confirmationDialog = new Dialog(getActivity());
        mClearButton = view.findViewById(R.id.clearSignatureButton);
        mSaveButton = view.findViewById(R.id.saveSignatureButton);

        TextView termsTV = view.findViewById(R.id.terms_textView);
        termsTV.setMovementMethod(new ScrollingMovementMethod());
        signaturePad = view.findViewById(R.id.signature_pad);

        confirmationDialog.setContentView(R.layout.signature_confirmation_dialog_layout);
        cancelBtn = confirmationDialog.findViewById(R.id.confirmation_cancel_button);
        confirmationBtn = confirmationDialog.findViewById(R.id.confirmation_confirm_button);
        homelessFirstName = confirmationDialog.findViewById(R.id.confirmation_first_name_ed);
        homelessLastName = confirmationDialog.findViewById(R.id.confirmation_last_name_ed);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clearSignatureButton:
                signaturePad.clear();
                break;

            case R.id.saveSignatureButton:
                if (signaturePad.isEmpty()) {
                   MainActivity.showErrorToast(getActivity(),getString(R.string.have_to_sign));
                }else {
                    confirmationPopUp();
                }
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mClearButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

        cancelBtn.setOnClickListener(this);
        confirmationBtn.setOnClickListener(this);

    }

    private void uploadJPEGToFirebase(File photo){
        Uri file = Uri.fromFile(photo);

        StorageReference ref = storageReference.child("homelessSignatures/"  + homelessFirstName.getText().toString() + " " + homelessLastName.getText().toString());
        ref.putFile(file)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        MainActivity.showSuccessToast(getActivity(),getString(R.string.success_upload));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();
                Toast.makeText(getActivity(), "Error:" + error, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void confirmationPopUp(){

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });

        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (homelessFirstName.getText().toString().isEmpty()){
                    homelessFirstName.setError(getString(R.string.empty_field_error));
                }else if (homelessLastName.getText().toString().isEmpty()){
                    homelessLastName.setError(getString(R.string.empty_field_error));
                }else{
                    Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                    verifyStoragePermissions(getActivity());

                    if (addJpgSignatureToGallery(signatureBitmap)) {
                        confirmationDialog.dismiss();
                        goToProfileFragment();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.unable_to_store_jpg), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        confirmationDialog.show();

    }

    private void goToProfileFragment(){
        ViewPager viewPager = getActivity().findViewById(R.id.create_homeless_view_pager);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstName", homelessFirstName.getText().toString());
        editor.putString("lastName", homelessLastName.getText().toString());
        editor.apply();

        int position = viewPager.getCurrentItem();

        position++;
        viewPager.setCurrentItem(position);

        //hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    private File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(getString(R.string.signature_pad), getString(R.string.directory_not_created));
        }
        return file;
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir(getString(R.string.signature_pad)), String.format(user.getEmail() + ".jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            uploadJPEGToFirebase(photo);

            /*Delete foto from gallery after upload on firebase*/
            photo.delete();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), getString(R.string.cannot_write_external), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static void verifyStoragePermissions(Activity activity) {
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
