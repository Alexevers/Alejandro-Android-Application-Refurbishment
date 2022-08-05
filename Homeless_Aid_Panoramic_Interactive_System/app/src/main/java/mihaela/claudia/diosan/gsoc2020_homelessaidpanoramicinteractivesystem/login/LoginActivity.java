package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.donor.HomeDonor;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.FirstActivityLG;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.MainActivityLG;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.register.RegisterActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer.HomeVolunteer;

public class LoginActivity extends MainActivity implements View.OnClickListener {

    /*TextViews*/
    TextView forgotPassword;

    /*Buttons*/
    Button loginBtn;
    MaterialButton signUp;
    MaterialButton liquid_galaxy;

    /*EditTexts*/
    TextInputEditText loginEmailEditText;
    TextInputEditText loginPasswordEditText;

    String loginEmailValue;
    String loginPasswordValue;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        AuxiliaryMethods.makeActivityFullScreen(getWindow(), getSupportActionBar());
        initViews();


        loginBtn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        liquid_galaxy.setOnClickListener(this);
    }

    private void initViews() {
        forgotPassword = findViewById(R.id.forgot_password_text_view);
        signUp = findViewById(R.id.signup);
        liquid_galaxy = findViewById(R.id.liquid_galaxy_tv);
        loginEmailEditText = findViewById(R.id.login_email_edit_text);
        loginPasswordEditText = findViewById(R.id.login_password_edit_text);
        loginBtn = findViewById(R.id.login_button);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_password_text_view:
                Intent forgotPassActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class );
                startActivity(forgotPassActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.signup:
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.liquid_galaxy_tv:
                Intent statisticsActivity = new Intent(LoginActivity.this, MainActivityLG.class);
                startActivity(statisticsActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.login_button:
                validateForm();
                login();

        }
    }


    public void login(){
        loginEmailValue = loginEmailEditText.getText().toString();
        loginPasswordValue = loginPasswordEditText.getText().toString();

        if (loginEmailValue.isEmpty() || loginPasswordValue.isEmpty()){
            loginEmailEditText.setError(getString(R.string.email_error_text));
        }else{
            mAuth.signInWithEmailAndPassword(loginEmailValue, loginPasswordValue)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setDialog(true);
                                isDonor(loginEmailValue);
                                isVolunteer(loginEmailValue);

                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                MainActivity.showErrorToast(LoginActivity.this, getString(R.string.error_login));
                            }

                        }
                    });
        }

    }

    private void isDonor(String email){
        DocumentReference donorsDocument = mFirestore.collection("donors").document(email);

        if (email.isEmpty()){
            loginEmailEditText.setError(getString(R.string.email_error_text));
        }else{
            donorsDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            Intent donorIntent = new Intent(LoginActivity.this, HomeDonor.class);
                            startActivity(donorIntent);
                        }
                    }
                }
            });
        }

    }


    private void isVolunteer(String email){
        DocumentReference volunteersDocument = mFirestore.collection("volunteers").document(email);

        if (email.isEmpty()){
            loginEmailEditText.setError(getString(R.string.email_error_text));
        }else{
            volunteersDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            Intent volunteerIntent = new Intent(LoginActivity.this, HomeVolunteer.class);
                            startActivity(volunteerIntent);
                        }
                    }
                }
            });
        }

    }

    private void validateForm(){
        if (loginEmailEditText.getText().toString().isEmpty()){
            loginEmailEditText.setError(getString(R.string.email_error_text));
        }

        if (loginPasswordEditText.getText().toString().isEmpty()){
            loginPasswordEditText.setError(getString(R.string.password_error_text));
        }
    }

    private void setDialog(boolean show){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress);
        Dialog dialog = builder.create();
        if (show)dialog.show();
        else dialog.dismiss();
    }


    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
    }

}
