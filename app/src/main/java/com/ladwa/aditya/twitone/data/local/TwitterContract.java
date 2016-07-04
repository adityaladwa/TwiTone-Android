package com.ladwa.aditya.twitone.data.local;

import android.provider.BaseColumns;

/**
 * Created by Aditya on 04-Jul-16.
 */
public final class TwitterContract {
    public TwitterContract() {
    }

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SCREEN_NAME = "screen_name";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFILE_IMAGE_URL = "profile_image_url";
        public static final String COLUMN_BANNER_URL = "banner_url";

        public static String getUserCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + "LONG NOT NULL PRIMARY KEY," +
                    COLUMN_NAME + "TEXT NOT NULL, " +
                    COLUMN_SCREEN_NAME + "TEXT NOT NULL, " +
                    COLUMN_PROFILE_IMAGE_URL + "TEXT NOT NULL, " +
                    COLUMN_BANNER_URL + "TEXT NOT NULL" + ");";
        }

        public static String getUserDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }
}
