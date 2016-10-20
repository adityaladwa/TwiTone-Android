package com.ladwa.aditya.twitone.settings;

import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.TwitoneApp;

import javax.inject.Inject;

/**
 * A Repository that gives access to themes
 * Created by Aditya on 20-Oct-16.
 */

public class SettingsRepository implements SettingsStore {

    private static SettingsRepository INSTANCE = null;

    @Inject
    SharedPreferences mSharedPreferences;

    private SettingsRepository() {
        TwitoneApp.getTwitterComponent().inject(this);
    }

    public static SettingsRepository getInstance() {
        if (INSTANCE == null)
            INSTANCE = new SettingsRepository();
        return INSTANCE;
    }

    @Override
    public String getTheme() {
        return mSharedPreferences.getString("pref_theme", "string");
    }
}
