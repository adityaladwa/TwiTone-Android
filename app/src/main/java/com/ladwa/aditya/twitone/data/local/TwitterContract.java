package com.ladwa.aditya.twitone.data.local;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.design.BuildConfig;

/**
 * A database contract for the App
 * Created by Aditya on 04-Jul-16.
 */
public final class TwitterContract {

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_USER = "user";

    public TwitterContract() {
    }

    public static abstract class User implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_USER_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_USER_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SCREEN_NAME = "screen_name";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFILE_IMAGE_URL = "profile_image_url";
        public static final String COLUMN_BANNER_URL = "banner_url";

        public static String getUserCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SCREEN_NAME + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_IMAGE_URL + " TEXT NOT NULL, " +
                    COLUMN_BANNER_URL + " TEXT NOT NULL" + ");";
        }

        public static String getUserDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
}
