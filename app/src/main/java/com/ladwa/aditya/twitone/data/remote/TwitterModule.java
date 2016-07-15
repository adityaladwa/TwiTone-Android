package com.ladwa.aditya.twitone.data.remote;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
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
    TwitterRepository providesTwitterRepository(Application context) {
        return TwitterRepository.getInstance(TwitterLocalDataStore.getInstance(context), TwitterRemoteDataSource.getInstance());
    }

    @Provides
    @Singleton
    TwitterRemoteDataSource providesTwitterRemoteDataSource() {
        return TwitterRemoteDataSource.getInstance();
    }

    @Provides
    @Singleton
    TwitterLocalDataStore providesTwitterLocalDataStore(Application context) {
        return TwitterLocalDataStore.getInstance(context);
    }

}
