package com.ladwa.aditya.twitone.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A Dagger Module which provides database helper
 * Created by Aditya on 04-Jul-16.
 */
@Module
public class DbModule {

    @Provides
    @Singleton
    @NonNull
    public SQLiteOpenHelper providesSqLiteOpenHelper(@NonNull Context context) {
        return new TwitterDbHelper(context);
    }
}
