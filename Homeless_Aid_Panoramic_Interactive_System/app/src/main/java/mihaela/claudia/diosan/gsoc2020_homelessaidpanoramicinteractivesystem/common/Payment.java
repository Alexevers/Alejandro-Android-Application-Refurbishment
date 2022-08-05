package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import me.abhinay.input.CurrencyEditText;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.MainActivity;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;

public class Payment extends MainActivity  {

    MaterialButton payBtn;
    CardForm cardForm;
    MaterialAlertDialogBuilder alertDialogBuilder;
    CurrencyEditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setupPaymentForm();
    }

    private void setupPaymentForm(){
        etInput = (CurrencyEditText) findViewById(R.id.etInput);
        etInput.setCurrency("€");
        etInput.setDelimiter(false);
        etInput.setSpacing(true);
        etInput.setDecimals(true);
        etInput.setSeparator(".");


        cardForm = findViewById(R.id.card_form);
        payBtn = findViewById(R.id.button_pay);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation(getString(R.string.paymant_phone_explanation))
                .setup(Payment.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()){
                    showAlertDialog();
                }else {
                    MainActivity.showErrorToast(Payment.this,getString(R.string.payment_toast_fail));
                }
            }
        });
    }

    private void showAlertDialog(){
        alertDialogBuilder = new MaterialAlertDialogBuilder(Payment.this);
        alertDialogBuilder.setTitle(getString(R.string.payment_confirm_title));
        alertDialogBuilder.setMessage(getString(R.string.payment_card__number) + cardForm.getCardNumber() + "\n" +
                getString(R.string.payment_card_expiration) + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                getString(R.string.payment_card_cvv) + cardForm.getCvv() + "\n" +
                getString(R.string.payment_postal_code) + cardForm.getPostalCode() + "\n" +
                getString(R.string.payment_phone_number) + cardForm.getMobileNumber());

        alertDialogBuilder.setPositiveButton( getString(R.string.payment_confirm_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(Payment.this,  getString(R.string.payment_toast_success), Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton( getString(R.string.payment_cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
