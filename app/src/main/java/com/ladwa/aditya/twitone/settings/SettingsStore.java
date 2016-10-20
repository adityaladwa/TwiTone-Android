package com.ladwa.aditya.twitone.settings;

import android.content.Context;

/**
 * Created by Aditya on 20-Oct-16.
 */

public interface SettingsStore {
    String getTheme();

    void setTheme(Context context);
}
