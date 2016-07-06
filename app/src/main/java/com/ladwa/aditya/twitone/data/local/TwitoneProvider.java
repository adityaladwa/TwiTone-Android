package com.ladwa.aditya.twitone.data.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.regex.Matcher;

/**
 * Created by Aditya on 06-Jul-16.
 */
public class TwitoneProvider extends ContentProvider {

    private static final int USER_ITEM = 100;

    private static final UriMatcher sURI_MATCHER = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TwitterContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,TwitterContract.PATH_USER+ "/#" ,USER_ITEM);
        return matcher;
    }

    private TwitterDbHelper mTwitterDbHelper;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
