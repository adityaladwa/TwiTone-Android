package com.ladwa.aditya.twitone.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterDataStore;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLiteDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLiteGetResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import rx.Observable;
import timber.log.Timber;

/**
 * A class that implements TwitterDataStore for Local Database
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterLocalDataStore implements TwitterDataStore {
    private static TwitterLocalDataStore INSTANCE;
    private static TwitterDbHelper mTwitterDbHelper;
    private static SQLiteDatabase db;
    private static StorIOSQLite mStorIOSQLite;

    private TwitterLocalDataStore(@NonNull Context context) {
        mTwitterDbHelper = new TwitterDbHelper(context);
        db = mTwitterDbHelper.getWritableDatabase();

        mStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(mTwitterDbHelper)
                .addTypeMapping(User.class, SQLiteTypeMapping.<User>builder()
                        .putResolver(new UserStorIOSQLitePutResolver())
                        .getResolver(new UserStorIOSQLiteGetResolver())
                        .deleteResolver(new UserStorIOSQLiteDeleteResolver())
                        .build()
                )
                .build();

    }

    public static TwitterLocalDataStore getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = new TwitterLocalDataStore(context);
        return INSTANCE;
    }

    @Override
    public Observable<com.ladwa.aditya.twitone.data.local.models.User> getUserInfo(long userID) {
        return mStorIOSQLite.get()
                .object(com.ladwa.aditya.twitone.data.local.models.User.class)
                .withQuery(Query.builder().table(TwitterContract.User.TABLE_NAME).build())
                .prepare()
                .asRxObservable();
    }


    public static void saveUserInfo(com.ladwa.aditya.twitone.data.local.models.User user) {
        ContentValues values = new ContentValues();
        values.put(TwitterContract.User.COLUMN_ID, user.getId());
        values.put(TwitterContract.User.COLUMN_NAME, user.getName());
        values.put(TwitterContract.User.COLUMN_SCREEN_NAME, user.getScreenName());
        values.put(TwitterContract.User.COLUMN_PROFILE_IMAGE_URL, user.getProfileUrl());
        values.put(TwitterContract.User.COLUMN_BANNER_URL, user.getBannerUrl());
        db.insert(TwitterContract.User.TABLE_NAME, null, values);
        Timber.d("Saved user to database" + user.getName());
    }

}
