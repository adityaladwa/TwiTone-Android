package com.ladwa.aditya.twitone.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.R;
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

    public static void destroyInstance() {
        INSTANCE = null;
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

    @Override
    public void setTheme(Context context) {
        String theme = getTheme();
        if (theme.equals(context.getResources().getStringArray(R.array.pref_theme_value)[0]))
            context.setTheme(R.style.AppTheme);
        else context.setTheme(R.style.AppThemeDark);
    }

    @Override
    public boolean isNotificationEnabled(Context context) {
        return mSharedPreferences.getBoolean(context.getString(R.string.pref_notification), true);
    }

    @Override
    public int getSyncDuration(Context context) {
        return 0;
    }

}
