package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.auxiliary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer.HomeVolunteer;

public class AuxiliaryMethods {



    public static void makeActivityFullScreen(Window window, ActionBar actionBar) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(actionBar).hide();
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() == 0){
            return true;
        }else if (target.length() < 6 || target.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static boolean isPasswordValid(CharSequence password){
        if (password.length() > 5) {
            return true;
        }
        return false;
    }

    public static boolean isUsernameValid(CharSequence username){
        if (username.length() > 3 && username.length() <=25) {
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(String email, String password) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.isEmpty();
    }

    public static boolean isLifeHistoryValid(CharSequence lifeHistory){
        if (lifeHistory.length() > 19 && lifeHistory.length()<=400) {
            return true;
        }
        return false;
    }

}
