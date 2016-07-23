package com.ladwa.aditya.twitone.data;

import com.ladwa.aditya.twitone.AppModule;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;
import com.ladwa.aditya.twitone.interactions.InteractionsFragment;
import com.ladwa.aditya.twitone.login.LoginActivityFragment;
import com.ladwa.aditya.twitone.mainscreen.MainScreenFragment;
import com.ladwa.aditya.twitone.message.MessageFragment;
import com.ladwa.aditya.twitone.trends.LocalTrendsFragment;
import com.ladwa.aditya.twitone.trends.Trends;
import com.ladwa.aditya.twitone.trends.TrendsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A dagger Component that is used to inject in various classes
 * Created by Aditya on 25-Jun-16.
 */
@Singleton
@Component(modules = {AppModule.class, TwitterModule.class})
public interface TwitterComponent {

    //Expose methods from TwitterModule
    void inject(LoginActivityFragment fragment);

    void inject(MainScreenFragment fragment);

    void inject(TwitterRemoteDataSource twitterRemoteDataSource);

    void inject(InteractionsFragment fragment);

    void inject(MessageFragment fragment);

    void inject(TrendsFragment fragment);

    void inject(LocalTrendsFragment fragment);

    void inject(Trends activity);

}
