package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

import static android.content.Context.MODE_PRIVATE;


public class NeedsFragment extends Fragment implements View.OnClickListener {

    /*TextViews*/
    private View view;
    private TextView needTV;
    private TextView needText;

    /*Buttons*/
    private MaterialButton cancelScheduleBtn;
    private MaterialButton saveScheduleBtn;

    /*SharedPreferences*/
    private SharedPreferences preferences;

    /*EditText*/
    private TextInputEditText scheduleEditText;

    /*ChipGroup*/
    private ChipGroup chipGroup;

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private FirebaseUser user;
    private Map<String,String> homeless = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_needs, container, false);

        preferences = getActivity().getSharedPreferences("homelessInfo", MODE_PRIVATE);

        initViews();
        firebaseInit();

        return view;
    }


    private void initViews(){
        scheduleEditText = view.findViewById(R.id.homeless_schedule_edit_text);
        needTV = view.findViewById(R.id.most_important_neev_tv);
        needText = view.findViewById(R.id.most_important_need);
        cancelScheduleBtn = view.findViewById(R.id.cancelScheduleButton);
        saveScheduleBtn = view.findViewById(R.id.saveScheduleButton);
        chipGroup = view.findViewById(R.id.chip_group);
    }

    private void firebaseInit(){
        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cancelScheduleBtn.setOnClickListener(this);
        saveScheduleBtn.setOnClickListener(this);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {
                // Handle the checked chip change.
                Chip chip = chipGroup.findViewById(checkedId);
                if(chip != null){
                    needTV.setVisibility(View.VISIBLE);
                    needText.setText(chip.getText().toString());
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelScheduleButton:
                deleteExistingInfo();
                startActivity(new Intent(getActivity(),HomeVolunteer.class));
                break;
            case R.id.saveScheduleButton:
                if (isValidForm()){
                    uploadDataToFirebase();
                    MainActivity.showSuccessToast(getActivity(), getString(R.string.account_created));
                    startActivity(new Intent(getActivity(), HomeVolunteer.class));
                }
        }

    }

    private boolean isValidForm(){
        if (scheduleEditText.getText().toString().isEmpty()){
            scheduleEditText.setError(getString(R.string.complete_schedule_toast));
            return false;
        }

        if (scheduleEditText.getText().length()>40){
            scheduleEditText.setError(getString(R.string.maxim_char_schedule));
            return false;
        }

        if (needText.getText().toString().isEmpty()){
            MainActivity.showErrorToast(getActivity(), getString(R.string.complete_need_toast));
            return false;
        }
        return true;
    }

    private void uploadDataToFirebase(){
        String homelessUsername = preferences.getString("homelessUsername","");

        homeless.put("homelessSchedule", scheduleEditText.getText().toString());
        homeless.put("homelessNeed", needText.getText().toString());

        mFirestore.collection("homeless").document(homelessUsername).set(homeless, SetOptions.merge());
    }


    private void deleteExistingInfo(){
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        String username = preferences.getString("homelessUsername", "");

        mFirestore.collection("homeless").document(username).delete();

        storageReference.child("homelessSignatures/" + firstName + " " + lastName).delete();

        storageReference.child("homelessProfilePhotos/" + user.getEmail() + "->" + username).delete();
    }



}
