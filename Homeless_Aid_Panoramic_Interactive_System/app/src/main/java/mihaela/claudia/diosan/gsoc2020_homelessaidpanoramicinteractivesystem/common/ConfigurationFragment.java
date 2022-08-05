package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;


public class ConfigurationFragment extends PreferenceFragmentCompat {

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;


    private EditTextPreference phonePreference;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.user_preferences, rootKey);

        phonePreference = (androidx.preference.EditTextPreference) findPreference("phone_volunteer");

        initFirebase();
    }

    private void initFirebase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        phonePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String document = user.getEmail();

                if (!AuxiliaryMethods.isValidPhoneNumber(newValue.toString())){
                    MainActivity.showErrorToast(getActivity(),getString(R.string.phone_error_text));
                }else{
                    changeVolunteer(document, newValue);
                    changeDonor(document,newValue);
                   // mFirestore.collection("volunteers").document(document).update("volunteerPhone", newValue);
                    MainActivity.showSuccessToast(getActivity(),getString(R.string.config_changed_phone_toast));
                }
                return false;
            }
        });
    }

    private void changeVolunteer(final String email, final Object newValue){
        DocumentReference volunteersDocument = mFirestore.collection("volunteers").document(email);

        volunteersDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        mFirestore.collection("volunteers").document(email).update("phone", newValue);
                    }
                }
            }
        });
    }

    private void changeDonor(final String email, final Object newValue){
        DocumentReference donorsDocument = mFirestore.collection("donors").document(email);

        donorsDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        mFirestore.collection("donors").document(email).update("phone", newValue);
                    }
                }
            }
        });
    }





}
