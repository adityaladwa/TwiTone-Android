package com.ladwa.aditya.twitone.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverType;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * A model for Tweets
 * Created by Aditya on 14-Jul-16.
 */
@AutoValue
@StorIOSQLiteType(table = TwitterContract.Tweet.TABLE_NAME)
@StorIOContentResolverType(uri = TwitterContract.Tweet.CONTENT_URI_STRING)
public class Tweet implements Parcelable {

    public Tweet() {
    }

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

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_USER_SCREEN_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_USER_SCREEN_NAME)
    String screenName;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_USER_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_USER_NAME)
    String userName;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_FAV_COUNT)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_FAV_COUNT)
    int favCount;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_IS_VERIFIED)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_IS_VERIFIED)
    int verified;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_RETWEET_COUNT)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_RETWEET_COUNT)
    int retweetCount;


    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_FAV)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_FAV)
    int fav;

    @StorIOSQLiteColumn(name = TwitterContract.Tweet.COLUMN_RETWEET)
    @StorIOContentResolverColumn(name = TwitterContract.Tweet.COLUMN_RETWEET)
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

    protected Tweet(Parcel in) {
        tweet = in.readString();
        lastModified = in.readString();
        dateCreated = in.readString();
        profileUrl = in.readString();
        screenName = in.readString();
        userName = in.readString();
        favCount = in.readInt();
        verified = in.readInt();
        retweetCount = in.readInt();
        fav = in.readInt();
        retweet = in.readInt();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tweet);
        dest.writeString(lastModified);
        dest.writeString(dateCreated);
        dest.writeString(profileUrl);
        dest.writeString(screenName);
        dest.writeString(userName);
        dest.writeInt(favCount);
        dest.writeInt(verified);
        dest.writeInt(retweetCount);
        dest.writeInt(fav);
        dest.writeInt(retweet);
    }
}
