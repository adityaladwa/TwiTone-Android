package com.ladwa.aditya.twitone.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ladwa.aditya.twitone.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
