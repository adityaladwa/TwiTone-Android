package com.ladwa.aditya.twitone.data.remote;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import twitter4j.Twitter;

/**
 * Created by Aditya on 24-Jun-16.
 */
@Module
public class TwitterModule {

    public static final HttpUrl TWITTER_API_URL = HttpUrl.parse("https://api.github.com/");


    @Provides
    @Singleton
    HttpUrl provideBaseUrl() {
        return TWITTER_API_URL;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
