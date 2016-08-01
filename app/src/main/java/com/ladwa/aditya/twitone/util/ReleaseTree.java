package com.ladwa.aditya.twitone.util;

import android.util.Log;

import timber.log.Timber;

/**
 * Created by Aditya on 01-Aug-16.
 */
public class ReleaseTree extends Timber.Tree {

    private static final int LINE_MAX_LENGTH = 4000;

    @Override
    protected boolean isLoggable(int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }

        return true;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        if (isLoggable(priority)) {
            if (message.length() < LINE_MAX_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message);
                } else {
                    Log.println(priority, tag, message);
                }
            }
        }
    }
}
