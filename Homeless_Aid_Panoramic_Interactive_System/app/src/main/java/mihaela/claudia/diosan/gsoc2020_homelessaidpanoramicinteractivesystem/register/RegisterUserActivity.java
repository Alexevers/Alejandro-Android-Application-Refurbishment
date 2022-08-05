package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.register;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.preference.PreferenceManager;

        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.text.method.LinkMovementMethod;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.material.button.MaterialButton;
        import com.google.android.material.dialog.MaterialAlertDialogBuilder;
        import com.google.android.material.textfield.TextInputEditText;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthUserCollisionException;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.HashMap;
        import java.util.Map;

        import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
        import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
        import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;
        import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login.LoginActivity;

public class RegisterUserActivity extends MainActivity implements View.OnClickListener {

    /* ImageView */
    ImageView regUserImg;

    /* Buttons */
    MaterialButton registerBtn;

    /* Alert Dialogs */
    Dialog succesRegisterDialog;

    /* TextViews */
    TextView acceptTermsTV;

    /* Check Box */
    CheckBox acceptTermsCheckbox;

    /* EditTexts*/
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText firstNameEditText;
    TextInputEditText lastNameEditText;
    TextInputEditText phoneEditText;

    String usernameValue;
    String emailValue;
    String firstNameValue;
    String lastNameValue;
    String phoneValue;

    SharedPreferences sharedPreferences;

    /*Firebase*/
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private Map<String,String> user = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initViews();
        initFirebase();

        registerBtn.setOnClickListener(this);
        acceptTermsCheckbox.setOnClickListener(this);
    }

    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void initViews(){
        regUserImg = findViewById(R.id.register_user_image);
        regUserImg.setImageResource(R.drawable.register_user_image);

        succesRegisterDialog = new Dialog(this);

        acceptTermsTV = findViewById(R.id.accept_terms_text_view);
        acceptTermsTV.setMovementMethod(LinkMovementMethod.getInstance());

        acceptTermsCheckbox = findViewById(R.id.user_terms_checkbox);

        registerBtn = findViewById(R.id.register_user_button);

        usernameEditText = findViewById(R.id.user_username_edit_text);
        emailEditText = findViewById(R.id.user_email_edit_text);
        passwordEditText = findViewById(R.id.user_password_edit_text);
        firstNameEditText = findViewById(R.id.user_first_name_edit_text);
        lastNameEditText = findViewById(R.id.user_last_name_edit_text);
        phoneEditText = findViewById(R.id.user_phone_edit_text);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.register_user_button:
                getInfo();
                registerUser();
                break;
            case R.id.user_terms_checkbox:
                if (acceptTermsCheckbox.isChecked()){
                    acceptTermsCheckbox.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    acceptTermsCheckbox.setTextColor(getResources().getColor(R.color.grey));
                }
        }
    }

    public void registerUser(){
        if (acceptTermsCheckbox.isChecked() && isValidForm()) {
            createEmailPasswordAccount(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }else if (!acceptTermsCheckbox.isChecked()){
            MainActivity.showErrorToast(RegisterUserActivity.this,getString(R.string.register_user_terms_error));
        }
    }

    public void createEmailPasswordAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showSuccessfulPopup();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                MainActivity.showErrorToast(RegisterUserActivity.this, getString(R.string.user_exists));
                            }else{
                                MainActivity.showErrorToast(RegisterUserActivity.this,getString(R.string.error_login));
                            }
                        }
                    }
                });
    }

    public void getInfo() {

        String userType = sharedPreferences.getString("userType", "");

        usernameValue = usernameEditText.getText().toString();
        emailValue = emailEditText.getText().toString();
        firstNameValue = firstNameEditText.getText().toString();
        lastNameValue = lastNameEditText.getText().toString();
        phoneValue = phoneEditText.getText().toString();


            user.put("username", usernameValue);
            user.put("email", emailValue);
            user.put("firstName", firstNameValue);
            user.put("lastName", lastNameValue);
            user.put("phone", phoneValue);

        if (emailValue.isEmpty()){
            emailEditText.setError(getString(R.string.email_error_text));
        }else{
            if (userType.equals("donor")){
                mFirestore.collection("donors").document(emailValue).set(user);
            }else if (userType.equals("volunteer")){
                mFirestore.collection("volunteers").document(emailValue).set(user);
            }

        }
    }

    private boolean isValidForm(){
        if (!AuxiliaryMethods.isValidPhoneNumber(phoneEditText.getText().toString())){
            phoneEditText.setError(getString(R.string.phone_error_text));
        }else if (!AuxiliaryMethods.isPasswordValid(passwordEditText.getText().toString())){
            passwordEditText.setError(getString(R.string.password_error_text));
        }else if (!AuxiliaryMethods.isUsernameValid(usernameEditText.getText().toString())){
            usernameEditText.setError(getString(R.string.username_error_text));
        }
        return  AuxiliaryMethods.isUsernameValid(usernameEditText.getText().toString()) && AuxiliaryMethods.isValidPhoneNumber(phoneEditText.getText().toString()) && AuxiliaryMethods.isPasswordValid(passwordEditText.getText().toString());
    }

    public void showSuccessfulPopup(){
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.register_pop_up_title))
                .setMessage(getString(R.string.register_pop_up_message))
                .setIcon(R.drawable.ic_check_circle_black_24dp)
                .setPositiveButton(getString(R.string.register_pop_up_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goLogin = new Intent(RegisterUserActivity.this, LoginActivity.class);
                        startActivity(goLogin);
                    }
                })

                .show();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
    }
}
