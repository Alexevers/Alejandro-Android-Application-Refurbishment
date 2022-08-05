package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary.AuxiliaryMethods;

public  class RegisterActivity extends MainActivity implements View.OnClickListener {


    /*Buttons*/
    MaterialButton knowMoreDonorBtn;
    MaterialButton knowMoreVolunteerBtn;
    MaterialButton startRegisterDonorBtn;
    MaterialButton startRegisterVolunteerBtn;

    /*Shared Preference*/
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

       /* AuxiliaryMethods.makeActivityFullScreen(getWindow(), getSupportActionBar());*/
        initViews();

        knowMoreDonorBtn.setOnClickListener(this);
        knowMoreVolunteerBtn.setOnClickListener(this);
        startRegisterDonorBtn.setOnClickListener(this);
        startRegisterVolunteerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.know_more_donor_button:
                MainActivity.showPositivePopup(this, getString(R.string.donor_know_more_title), getString(R.string.donor_know_more_text), getString(R.string.register_pop_up_button));
                break;
            case R.id.start_register_donor:
                startActivity(new Intent(RegisterActivity.this, RegisterUserActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userType", "donor").apply();
                break;
            case R.id.start_register_volunteer:
                startActivity(new Intent(RegisterActivity.this, RegisterUserActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString("userType", "volunteer").apply();
                break;
            case R.id.know_more_volunteer_button:
                MainActivity.showPositivePopup(this,getString(R.string.volunteer_know_more_title), getString(R.string.volunteer_know_more_text),getString(R.string.register_pop_up_button) );
                break;
        }
    }

    private void initViews() {
        knowMoreDonorBtn = findViewById(R.id.know_more_donor_button);
        startRegisterDonorBtn = findViewById(R.id.start_register_donor);
        knowMoreVolunteerBtn = findViewById(R.id.know_more_volunteer_button);
        startRegisterVolunteerBtn = findViewById(R.id.start_register_volunteer);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
