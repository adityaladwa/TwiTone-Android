package com.ladwa.aditya.twitone.ui.settings;

import android.content.Context;

/**
 * An Interface that defines various setting that a yser can change
 * Created by Aditya on 20-Oct-16.
 */

public interface SettingsStore {
    String getTheme();

    void setTheme(Context context);

    boolean isNotificationEnabled(Context context);

    int getSyncDuration(Context context);
}
