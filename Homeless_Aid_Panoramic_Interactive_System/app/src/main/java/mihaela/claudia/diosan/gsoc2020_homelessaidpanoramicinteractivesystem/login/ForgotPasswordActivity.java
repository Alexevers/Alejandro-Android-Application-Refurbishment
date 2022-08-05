package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;

public class ForgotPasswordActivity extends MainActivity implements View.OnClickListener {

    /*Buttons*/
    Button recoverPassword;

    /*EditTexts*/
    TextInputEditText forgotPasswordEmail;

    /*Firebase*/
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        initViews();

        recoverPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recover_password_button){
            if (AuxiliaryMethods.isEmailValid(forgotPasswordEmail.getText().toString(), forgotPasswordEmail.getText().toString())){
                resetPassword(forgotPasswordEmail.getText().toString());
            }else{
                forgotPasswordEmail.setError(getString(R.string.fp_recover_failed));
               // Toast.makeText(ForgotPasswordActivity.this,getString(R.string.fp_recover_failed), Toast.LENGTH_SHORT).show();

              //  MainActivity.showErrorToast(ForgotPasswordActivity.this,getString(R.string.is_email_valid_error));
            }
        }
    }

    private void initViews(){
        recoverPassword = findViewById(R.id.recover_password_button);
        forgotPasswordEmail = findViewById(R.id.forgot_password_email);
    }



    private void resetPassword(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showPopUp();
                        } else {
                            forgotPasswordEmail.setError(getString(R.string.fp_recover_failed));
                          //  Toast.makeText(ForgotPasswordActivity.this,getString(R.string.fp_recover_failed), Toast.LENGTH_SHORT).show();
                        //    MainActivity.showErrorToast(ForgotPasswordActivity.this,getString(R.string.fp_recover_failed));
                        }
                    }
                });
    }



    public void showPopUp(){
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.pop_up_title))
                .setMessage(getString(R.string.pop_up_message))
                .setIcon(R.drawable.ic_check_circle_black_24dp)
                .setPositiveButton(getString(R.string.pop_up_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
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
