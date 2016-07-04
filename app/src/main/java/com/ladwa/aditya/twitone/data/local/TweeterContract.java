package com.ladwa.aditya.twitone.data.local;

import android.provider.BaseColumns;

/**
 * Created by Aditya on 04-Jul-16.
 */
public final class TweeterContract {
    public TweeterContract() {
    }

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SCREEN_NAME = "screen_name";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFILE_IMAGE_URL = "profile_image_url";
    }
}
