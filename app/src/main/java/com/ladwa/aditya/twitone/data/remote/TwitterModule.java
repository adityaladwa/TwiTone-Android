package com.ladwa.aditya.twitone.data.remote;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Aditya on 24-Jun-16.
 */
@Module
public class TwitterModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Twitter providesTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("s01kRJpmpGr783CSY6pQCs2Nc");
        builder.setOAuthConsumerSecret("CdxuKvF3QKoT34wWoYkslvlX3B80UU4mpg2btZXG2byWNXd3P6");
        Configuration configuration = builder.build();
        return new TwitterFactory(configuration).getInstance();
    }

    @Provides
    @Singleton
    AsyncTwitter providesTwitterAsync() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("s01kRJpmpGr783CSY6pQCs2Nc");
        builder.setOAuthConsumerSecret("CdxuKvF3QKoT34wWoYkslvlX3B80UU4mpg2btZXG2byWNXd3P6");
        Configuration configuration = builder.build();
        return new AsyncTwitterFactory(configuration).getInstance();
    }
}
