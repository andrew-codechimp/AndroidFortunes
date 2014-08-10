package org.codechimp.androidfortunes;


import android.os.Bundle;
import android.preference.PreferenceFragment;

public class GeneralUserPreferencesFragment extends PreferenceFragment {

    private static final String TAG = "GeneralUserPreferenceFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_user_preferences);
    }
}

