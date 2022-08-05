package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy;

import androidx.preference.PreferenceManager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.EditText;
import android.widget.Toast;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lg_settings_preferences);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        slavesPrefs();

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.za
        bindPreferenceSummaryToValue(findPreference("SSH-USER"));
        bindPreferenceSummaryToValue(findPreference("SSH-PASSWORD"));
        bindPreferenceSummaryToValue(findPreference("SSH-IP"));
        bindPreferenceSummaryToValue(findPreference("SSH-PORT"));

      /*  lg_user = (EditTextPreference) findPreference("SSH-USER");
        lg_password = (EditTextPreference) findPreference("SSH-PASSWORD");
        lg_ip = (EditTextPreference) findPreference("SSH-IP");
        lg_port = (EditTextPreference) findPreference("SSH-PORT");;*/


    }

    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        LGConnectionManager.getInstance().setData(prefs.getString("SSH-USER", "lg"), prefs.getString("SSH-PASSWORD", "lqgalaxy"), prefs.getString("SSH-IP", "192.168.1.76"), Integer.parseInt(prefs.getString("SSH-PORT", "22")));
    }
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        LGConnectionManager.getInstance().setData(prefs.getString("SSH-USER", "lg"), prefs.getString("SSH-PASSWORD", "lqgalaxy"), prefs.getString("SSH-IP", "192.168.1.76"), Integer.parseInt(prefs.getString("SSH-PORT", "22")));
        Toast.makeText(SettingsActivity.this, "Connectivity settings changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceChange(android.preference.Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            android.preference.ListPreference listPreference = (android.preference.ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference.getKey().toLowerCase().contains("password")) {
            EditText edit = ((EditTextPreference) preference).getEditText();
            String pref = edit.getTransformationMethod().getTransformation(stringValue, edit).toString();
            preference.setSummary(pref);

        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }

        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
            onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    private void slavesPrefs(){

       // final androidx.preference.ListPreference logosPrefs =  findPreference("logos_preference");
        final ListPreference logosPrefs = (ListPreference) findPreference("logos_preference");
        final ListPreference homelessPrefs = (ListPreference) findPreference("homeless_preference");
        final ListPreference localPrefs = (ListPreference) findPreference("local_preference");
        final ListPreference globalPrefs = (ListPreference) findPreference("global_preference");
      //  final ListPreference liveOverviewPrefs = (ListPreference) findPreference("live_overview_homeless");

        logosPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preferences.edit().putString("logos_preference", newValue.toString()).apply();
                preferences.edit().putString("homeless_preference", newValue.toString()).apply();
                preferences.edit().putString("local_preference", newValue.toString()).apply();
                preferences.edit().putString("global_preference", newValue.toString()).apply();
                logosPrefs.setValue(newValue.toString());
                homelessPrefs.setValue(newValue.toString());
                localPrefs.setValue(newValue.toString());
                globalPrefs.setValue(newValue.toString());
          //      liveOverviewPrefs.setValue(newValue.toString());

                return false;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Això és per a que un cop entrem a settings, al tornar enrere(osigui a MainActivityLG) continui tal qual estava, és a dir,
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}