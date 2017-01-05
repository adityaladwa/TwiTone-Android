package com.ladwa.aditya.twitone.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;
import com.ladwa.aditya.twitone.injection.ApplicationContext;
import com.ladwa.aditya.twitone.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }


    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    Twitter providesTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Constants.OAUTH_COMSUMER_KEY);
        builder.setOAuthConsumerSecret(Constants.OAUTH_CONSUMER_SECRET);
        Configuration configuration = builder.build();
        return new TwitterFactory(configuration).getInstance();
    }

    @Provides
    @Singleton
    AsyncTwitter providesTwitterAsync() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Constants.OAUTH_COMSUMER_KEY);
        builder.setOAuthConsumerSecret(Constants.OAUTH_CONSUMER_SECRET);
        Configuration configuration = builder.build();
        return new AsyncTwitterFactory(configuration).getInstance();
    }


    @Provides
    @Singleton
    TwitterRemoteDataSource providesTwitterRemoteDataSource() {
        return new TwitterRemoteDataSource();
    }

    @Provides
    @Singleton
    TwitterLocalDataStore providesTwitterLocalDataStore(Application context) {
        return new TwitterLocalDataStore(context);
    }
}
