package com.ladwa.aditya.twitone.data.local.models;

import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverType;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * A model for User profile
 * Created by Aditya on 04-Jul-16.
 */
@StorIOSQLiteType(table = TwitterContract.User.TABLE_NAME)
@StorIOContentResolverType(uri = TwitterContract.User.CONTENT_URI_STRING)
public class User {


    public User() {
    }

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_ID, key = true)
    @StorIOContentResolverColumn(name = TwitterContract.User.COLUMN_ID, key = true)
    Long id;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.User.COLUMN_NAME)
    String name;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_SCREEN_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.User.COLUMN_SCREEN_NAME)
    String screenName;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_PROFILE_IMAGE_URL)
    @StorIOContentResolverColumn(name = TwitterContract.User.COLUMN_PROFILE_IMAGE_URL)
    String profileUrl;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_BANNER_URL)
    @StorIOContentResolverColumn(name = TwitterContract.User.COLUMN_BANNER_URL)
    String bannerUrl;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_LAST_MODIFIED)
    @StorIOContentResolverColumn(name = TwitterContract.User.COLUMN_LAST_MODIFIED)
    String lastModified;

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}
