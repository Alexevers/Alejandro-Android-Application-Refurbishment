package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.donor.HomeDonor;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer.HomeVolunteer;

public class ContactFragment extends Fragment implements View.OnClickListener{

    private TextInputEditText subjectEditText;
    private TextInputEditText messageEditText;

    private MaterialButton contactBtn;

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    FirebaseUser user;

    private Map<String,String> contactFormData = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initViews(view);
        initFirebase();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contactBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.contact_send_button){
            if (validFields()){
                sendContactFormData();
                showSendMessageToast(getString(R.string.contact_toast_message));

                isVolunteer(user.getEmail());
                isDonor(user.getEmail());
              /*  startActivity(new Intent(getContext(), HomeVolunteer.class));*/
            }
        }
    }

    private void isVolunteer(String email){
        DocumentReference donorsDocument = mFirestore.collection("volunteers").document(email);

            donorsDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            Intent volunteerIntent = new Intent(getContext(), HomeVolunteer.class);
                            startActivity(volunteerIntent);
                        }
                    }
                }
            });
        }

    private void isDonor(String email){
        DocumentReference donorsDocument = mFirestore.collection("donors").document(email);

        donorsDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        Intent donorIntent = new Intent(getContext(), HomeDonor.class);
                        startActivity(donorIntent);
                    }
                }
            }
        });
    }



    private void initFirebase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void sendContactFormData(){
        String subject = subjectEditText.getText().toString();
        String message = subjectEditText.getText().toString();

        contactFormData.put("subject", subject);
        contactFormData.put("message",message);

        mFirestore.collection("contactForm").document(user.getEmail() + "\n" +  subject).set(contactFormData, SetOptions.merge());

    }

    private boolean validFields() {
        if (subjectEditText.getText().toString().isEmpty()) {
            subjectEditText.setError(getString(R.string.empty_field_error));
            return false;
        }

        if (messageEditText.getText().toString().isEmpty()) {
            messageEditText.setError(getString(R.string.empty_field_error));
            return false;
        }
        return true;
    }

    private void initViews(View view) {
        contactBtn = view.findViewById(R.id.contact_send_button);
        subjectEditText = view.findViewById(R.id.contact_subject_hint);
        messageEditText = view.findViewById(R.id.form_contact_message_hint);
    }



    private void showSendMessageToast(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        View view =toast.getView();
        view.setBackgroundColor(Color.WHITE);
        TextView toastMessage =  toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.GREEN);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setTextSize(15);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.message_toast, 0,0,0);
        toastMessage.setPadding(10,10,10,10);
        toast.show();
    }

}
