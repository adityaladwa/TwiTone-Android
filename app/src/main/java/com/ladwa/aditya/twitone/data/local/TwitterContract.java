package com.ladwa.aditya.twitone.data.local;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * A database contract for the App
 * Created by Aditya on 04-Jul-16.
 */
public final class TwitterContract {

    public static final String CONTENT_AUTHORITY = "com.ladwa.aditya.twitone";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);

    //Content Uri paths
    public static final String PATH_USER = "user";
    public static final String PATH_TWEET = "tweet";
    public static final String PATH_INTERACTION = "interaction";
    public static final String PATH_DIRECT_MESSAGE = "directmessage";
    public static final String PATH_TRENDS = "trends";

    public TwitterContract() {
    }

    public static abstract class User implements BaseColumns {
        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_USER_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_USER_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SCREEN_NAME = "screen_name";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFILE_IMAGE_URL = "profile_image_url";
        public static final String COLUMN_BANNER_URL = "banner_url";
        public static final String COLUMN_LAST_MODIFIED = "last_modified";

        public static String getUserCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SCREEN_NAME + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_IMAGE_URL + " TEXT NOT NULL, " +
                    COLUMN_LAST_MODIFIED + " TEXT NOT NULL, " +
                    COLUMN_BANNER_URL + " TEXT NOT NULL" + ");";
        }

        public static String getUserDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static abstract class Tweet implements BaseColumns {

        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_TWEET;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_TWEET_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TWEET;
        public static final String CONTENT_TWEET_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TWEET;

        public static final String TABLE_NAME = "tweet";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TWEET = "tweet_text";
        public static final String COLUMN_LAST_MODIFIED = "last_modified";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_PROFILE_URL = "profile_url";
        public static final String COLUMN_USER_SCREEN_NAME = "screen_name";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_IS_VERIFIED = "verified";
        public static final String COLUMN_FAV_COUNT = "fav_count";
        public static final String COLUMN_RETWEET_COUNT = "retweet_count";
        public static final String COLUMN_FAV = "fav";
        public static final String COLUMN_RETWEET = "retweet";


        public static String getTweetCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    COLUMN_TWEET + " TEXT NOT NULL, " +
                    COLUMN_LAST_MODIFIED + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_URL + " TEXT NOT NULL, " +
                    COLUMN_USER_SCREEN_NAME + " TEXT NOT NULL, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL, " +
                    COLUMN_FAV_COUNT + " INT NOT NULL, " +
                    COLUMN_RETWEET_COUNT + " INT NOT NULL, " +
                    COLUMN_FAV + " INT NOT NULL, " +
                    COLUMN_RETWEET + " INT NOT NULL, " +
                    COLUMN_IS_VERIFIED + " INT NOT NULL, " +
                    COLUMN_DATE_CREATED + " TEXT NOT NULL);";
        }


        public static String getTweetDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static Uri buildTweetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class Interaction implements BaseColumns {
        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_INTERACTION;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_INTERACTION_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_INTERACTION;
        public static final String CONTENT_INTERACTION_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_INTERACTION;
        public static final String TABLE_NAME = "interaction";


        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TWEET = "tweet_text";
        public static final String COLUMN_LAST_MODIFIED = "last_modified";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_PROFILE_URL = "profile_url";
        public static final String COLUMN_USER_SCREEN_NAME = "screen_name";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_IS_VERIFIED = "verified";
        public static final String COLUMN_FAV_COUNT = "fav_count";
        public static final String COLUMN_RETWEET_COUNT = "retweet_count";
        public static final String COLUMN_FAV = "fav";
        public static final String COLUMN_RETWEET = "retweet";


        public static String getInteractionCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    COLUMN_TWEET + " TEXT NOT NULL, " +
                    COLUMN_LAST_MODIFIED + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_URL + " TEXT NOT NULL, " +
                    COLUMN_USER_SCREEN_NAME + " TEXT NOT NULL, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL, " +
                    COLUMN_FAV_COUNT + " INT NOT NULL, " +
                    COLUMN_RETWEET_COUNT + " INT NOT NULL, " +
                    COLUMN_FAV + " INT NOT NULL, " +
                    COLUMN_RETWEET + " INT NOT NULL, " +
                    COLUMN_IS_VERIFIED + " INT NOT NULL, " +
                    COLUMN_DATE_CREATED + " TEXT NOT NULL);";
        }


        public static String getInteractionDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static Uri buildTweetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static abstract class DirectMessage implements BaseColumns {
        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_DIRECT_MESSAGE;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_INTERACTION_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DIRECT_MESSAGE;
        public static final String CONTENT_INTERACTION_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DIRECT_MESSAGE;
        public static final String TABLE_NAME = "directmessage";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_PROFILE_URL = "profile_url";
        public static final String COLUMN_RECIPIENT = "recipient";
        public static final String COLUMN_RECIPIENT_ID = "recipient_id";
        public static final String COLUMN_RECIPIENT_SCREEN_NAME = "recipient_screen_name";
        public static final String COLUMN_SENDER = "sender";
        public static final String COLUMN_SENDER_ID = "sender_id";
        public static final String COLUMN_SENDER_SCREEN_NAME = "sender_screen_name";
        public static final String COLUMN_TEXT = "text";

        public static String getDirectMessageCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    COLUMN_RECIPIENT + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_URL + " TEXT NOT NULL, " +
                    COLUMN_RECIPIENT_ID + " LONG NOT NULL, " +
                    COLUMN_RECIPIENT_SCREEN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SENDER + " TEXT NOT NULL, " +
                    COLUMN_SENDER_ID + " LONG NOT NULL, " +
                    COLUMN_SENDER_SCREEN_NAME + " TEXT NOT NULL, " +
                    COLUMN_TEXT + " TEXT NOT NULL, " +
                    COLUMN_DATE_CREATED + " TEXT NOT NULL);";
        }

        public static String getDirectMessageDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static Uri buildTweetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class Trends implements BaseColumns {
        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_TRENDS;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_INTERACTION_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TRENDS;
        public static final String CONTENT_INTERACTION_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TRENDS;
        public static final String TABLE_NAME = "trends";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_TREND = "trend";
        public static final String COLUMN_LOCAL = "local";

        public static String getTrendsCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_TREND + " TEXT NOT NULL PRIMARY KEY, " +
                    COLUMN_LOCAL + " INT NOT NULL, " +
                    COLUMN_DATE_CREATED + " TEXT NOT NULL);";

        }

        public static String getTrendsDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

        public static Uri buildTrendUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
