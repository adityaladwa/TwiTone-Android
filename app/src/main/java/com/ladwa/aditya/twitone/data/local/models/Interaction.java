package com.ladwa.aditya.twitone.data.local.models;

import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverType;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * A Model for Interactions
 * Created by Aditya on 19-Jul-16.
 */
@StorIOSQLiteType(table = TwitterContract.Interaction.TABLE_NAME)
@StorIOContentResolverType(uri = TwitterContract.Interaction.CONTENT_URI_STRING)
public class Interaction {

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_ID, key = true)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_ID, key = true)
    Long id;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_TWEET)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_TWEET)
    String tweet;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_LAST_MODIFIED)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_LAST_MODIFIED)
    String lastModified;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_DATE_CREATED)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_DATE_CREATED)
    String dateCreated;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_PROFILE_URL)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_PROFILE_URL)
    String profileUrl;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_USER_SCREEN_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_USER_SCREEN_NAME)
    String screenName;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_USER_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_USER_NAME)
    String userName;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_FAV_COUNT)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_FAV_COUNT)
    int favCount;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_IS_VERIFIED)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_IS_VERIFIED)
    int verified;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_RETWEET_COUNT)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_RETWEET_COUNT)
    int retweetCount;


    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_FAV)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_FAV)
    int fav;

    @StorIOSQLiteColumn(name = TwitterContract.Interaction.COLUMN_RETWEET)
    @StorIOContentResolverColumn(name = TwitterContract.Interaction.COLUMN_RETWEET)
    int retweet;

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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getRetweet() {
        return retweet;
    }

    public void setRetweet(int retweet) {
        this.retweet = retweet;
    }
}
