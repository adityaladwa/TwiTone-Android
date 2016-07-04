package com.ladwa.aditya.twitone.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterDataStore;

import rx.Observable;
import timber.log.Timber;
import twitter4j.User;

/**
 * A class that implements TwitterDataStore for Local Database
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterLocalDataStore implements TwitterDataStore {
    private static TwitterLocalDataStore INSTANCE;
    private static TwitterDbHelper mTwitterDbHelper;
    private static SQLiteDatabase db;


    private TwitterLocalDataStore(@NonNull Context context) {
        mTwitterDbHelper = new TwitterDbHelper(context);
        db = mTwitterDbHelper.getWritableDatabase();
    }

    public static TwitterLocalDataStore getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = new TwitterLocalDataStore(context);
        return INSTANCE;
    }

    @Override
    public Observable<User> getUserInfo(long userID) {

        return null;
    }


    public static void saveUserInfo(User user) {
        ContentValues values = new ContentValues();
        values.put(TwitterContract.User.COLUMN_ID, user.getId());
        values.put(TwitterContract.User.COLUMN_NAME, user.getName());
        values.put(TwitterContract.User.COLUMN_SCREEN_NAME, user.getScreenName());
        values.put(TwitterContract.User.COLUMN_PROFILE_IMAGE_URL, user.getOriginalProfileImageURL());
        values.put(TwitterContract.User.COLUMN_BANNER_URL, user.getProfileBannerMobileRetinaURL());
        db.insert(TwitterContract.User.TABLE_NAME, null, values);
        Timber.d("Saved user to database" + user.getName());
    }
}
