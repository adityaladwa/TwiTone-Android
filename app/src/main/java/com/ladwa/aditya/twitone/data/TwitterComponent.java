package com.ladwa.aditya.twitone.data;

import com.ladwa.aditya.twitone.AppModule;
import com.ladwa.aditya.twitone.data.remote.TwitterModule;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;
import com.ladwa.aditya.twitone.data.sync.SyncAdapter;
import com.ladwa.aditya.twitone.ui.interactions.InteractionsFragment;
import com.ladwa.aditya.twitone.ui.interactions.InteractionsPresenter;
import com.ladwa.aditya.twitone.ui.login.LoginActivityFragment;
import com.ladwa.aditya.twitone.ui.mainscreen.MainScreenFragment;
import com.ladwa.aditya.twitone.ui.mainscreen.MainScreenPresenter;
import com.ladwa.aditya.twitone.ui.message.MessageFragment;
import com.ladwa.aditya.twitone.ui.message.MessagePresenter;
import com.ladwa.aditya.twitone.ui.messagecompose.MessageComposeFragment;
import com.ladwa.aditya.twitone.ui.messagecompose.MessageComposePresenter;
import com.ladwa.aditya.twitone.ui.settings.SettingsRepository;
import com.ladwa.aditya.twitone.ui.trends.LocalTrendsFragment;
import com.ladwa.aditya.twitone.ui.trends.Trends;
import com.ladwa.aditya.twitone.ui.trends.TrendsFragment;
import com.ladwa.aditya.twitone.ui.trends.TrendsPresenter;
import com.ladwa.aditya.twitone.ui.tweet.Tweet;

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

    void inject(SettingsRepository repository);

    void inject(MainScreenFragment fragment);

    void inject(MainScreenPresenter presenter);

    void inject(TwitterRemoteDataSource twitterRemoteDataSource);

    void inject(InteractionsFragment fragment);

    void inject(MessageFragment fragment);

    void inject(MessagePresenter presenter);

    void inject(TrendsFragment fragment);

    void inject(LocalTrendsFragment fragment);

    void inject(TrendsPresenter trendsPresenter);

    void inject(Trends activity);

    void inject(MessageComposeFragment fragment);
    void inject(MessageComposePresenter presenter);

    void inject(SyncAdapter syncAdapter);

    void inject(InteractionsPresenter presenter);

    void inject(Tweet tweet);

}
