package com.ladwa.aditya.twitone.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * A class that implements TwitterDataStore for Local Database
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterLocalDataStore {
    private static TwitterLocalDataStore INSTANCE;
    private TwitterDbHelper mTwitterDbHelper;


    private TwitterLocalDataStore(@NonNull Context context) {
        mTwitterDbHelper = new TwitterDbHelper(context);
        mTwitterDbHelper.getWritableDatabase();

    }

    public static TwitterLocalDataStore getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = new TwitterLocalDataStore(context);
        return INSTANCE;
    }

}
