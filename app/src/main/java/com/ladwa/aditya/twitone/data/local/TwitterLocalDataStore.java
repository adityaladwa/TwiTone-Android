package com.ladwa.aditya.twitone.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterDataStore;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverGetResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverPutResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLiteDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLiteGetResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOSQLitePutResolver;
import com.pushtorefresh.storio.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import rx.Observable;

/**
 * A class that implements TwitterDataStore for Local Database
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterLocalDataStore implements TwitterDataStore {
    private static TwitterLocalDataStore INSTANCE;
    private static TwitterDbHelper mTwitterDbHelper;
    private static SQLiteDatabase db;
    private static StorIOSQLite mStorIOSQLite;
    private static StorIOContentResolver mStorIOContentResolver;

    private TwitterLocalDataStore(@NonNull Context context) {
        mTwitterDbHelper = new TwitterDbHelper(context);
        db = mTwitterDbHelper.getWritableDatabase();

        mStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(mTwitterDbHelper)
                .addTypeMapping(User.class, SQLiteTypeMapping.<User>builder()
                        .putResolver(new UserStorIOSQLitePutResolver())
                        .getResolver(new UserStorIOSQLiteGetResolver())
                        .deleteResolver(new UserStorIOSQLiteDeleteResolver())
                        .build())
                .build();

        mStorIOContentResolver = DefaultStorIOContentResolver.builder()
                .contentResolver(context.getContentResolver())
                .addTypeMapping(User.class, ContentResolverTypeMapping.<User>builder()
                        .putResolver(new UserStorIOContentResolverPutResolver())
                        .getResolver(new UserStorIOContentResolverGetResolver())
                        .deleteResolver(new UserStorIOContentResolverDeleteResolver())
                        .build())
                .build();
    }

    public static TwitterLocalDataStore getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = new TwitterLocalDataStore(context);
        return INSTANCE;
    }

    @Override
    public Observable<User> getUserInfo(long userID) {
//        return mStorIOSQLite.get()
//                .object(User.class)
//                .withQuery(Query.builder()
//                        .table(TwitterContract.User.TABLE_NAME)
//                        .where(TwitterContract.User.COLUMN_ID + " = ? ")
//                        .whereArgs(userID)
//                        .build()
//                )
//                .prepare()
//                .asRxObservable();

        return mStorIOContentResolver.get()
                .object(User.class)
                .withQuery(com.pushtorefresh.storio.contentresolver.queries.Query.builder()
                        .uri(TwitterContract.User.CONTENT_URI)
                        .where(TwitterContract.User.COLUMN_ID + " = ?")
                        .whereArgs(userID)
                        .build())
                .prepare()
                .asRxObservable();
    }


    public static void saveUserInfo(User user) {

//        mStorIOSQLite.put().object(user).prepare().executeAsBlocking();

        mStorIOContentResolver.put().object(user).prepare().executeAsBlocking();

    }

}
