package com.ladwa.aditya.twitone.data.local.models;

import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverType;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * A model for Tweets
 * Created by Aditya on 14-Jul-16.
 */
@StorIOSQLiteType(table = TwitterContract.Tweet.TABLE_NAME)
@StorIOContentResolverType(uri = TwitterContract.Tweet.CONTENT_URI_STRING)
public class Tweet {

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_ID, key = true)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_ID, key = true)
    Long id;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_TWEET)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_TWEET)
    String tweet;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_LAST_MODIFIED)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_LAST_MODIFIED)
    String lastModified;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_DATE_CREATED)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_DATE_CREATED)
    String dateCreated;

}
