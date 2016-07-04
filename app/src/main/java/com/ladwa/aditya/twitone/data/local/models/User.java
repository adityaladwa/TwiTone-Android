package com.ladwa.aditya.twitone.data.local.models;

import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Aditya on 04-Jul-16.
 */
@StorIOSQLiteType(table = TwitterContract.User.TABLE_NAME)
public class User {


    public User() {
    }

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_ID,key = true)
    Long id;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_NAME)
    String name;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_SCREEN_NAME)
    String screenName;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_PROFILE_IMAGE_URL)
    String profileUrl;

    @StorIOSQLiteColumn(name = TwitterContract.User.COLUMN_BANNER_URL)
    String bannerUrl;

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
