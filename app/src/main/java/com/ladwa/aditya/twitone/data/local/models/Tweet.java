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

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_PROFILE_URL)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_PROFILE_URL)
    String profileUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
