package com.ladwa.aditya.twitone.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

/**
 * A Database Helper for the app
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Twitone.db";
    public static final int DATABASE_VERSION = 2;

    public TwitterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TwitterContract.User.getUserCreateQuery());
        db.execSQL(TwitterContract.Tweet.getTweetCreateQuery());
        db.execSQL(TwitterContract.Interaction.getInteractionCreateQuery());
        db.execSQL(TwitterContract.DirectMessage.getDirectMessageCreateQuery());
        db.execSQL(TwitterContract.Trends.getTrendsCreateQuery());
        Timber.d("Created database" + DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TwitterContract.User.getUserDeleteQuery());
        db.execSQL(TwitterContract.Tweet.getTweetDeleteQuery());
        db.execSQL(TwitterContract.Interaction.getInteractionDeleteQuery());
        db.execSQL(TwitterContract.DirectMessage.getDirectMessageDeleteQuery());
        db.execSQL(TwitterContract.Trends.getTrendsDeleteQuery());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TwitterContract.User.getUserDeleteQuery());
        db.execSQL(TwitterContract.Tweet.getTweetDeleteQuery());
        db.execSQL(TwitterContract.Interaction.getInteractionDeleteQuery());
        db.execSQL(TwitterContract.DirectMessage.getDirectMessageDeleteQuery());
        db.execSQL(TwitterContract.Trends.getTrendsDeleteQuery());
        onCreate(db);
    }
}
