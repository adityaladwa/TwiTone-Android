package com.ladwa.aditya.twitone.data.remote;

import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterDataStore;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/**
 * A class that fetches Data from Remote Web service
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterRemoteDataSource implements TwitterDataStore {

    @Inject
    Twitter mTwitter;

    @Inject
    SharedPreferences preferences;

    private static TwitterRemoteDataSource INSTANCE = null;
    private TwitterRemoteDataSource() {
        TwitoneApp.getTwitterComponent().inject(this);
        long id = preferences.getLong(TwitoneApp.getInstance().getString(R.string.pref_userid), 0);
        String token = preferences.getString(TwitoneApp.getInstance().getString(R.string.pref_access_token), "");
        String secreat = preferences.getString(TwitoneApp.getInstance().getString(R.string.pref_access_secret), "");
        AccessToken accessToken = new AccessToken(token, secreat);
        mTwitter.setOAuthAccessToken(accessToken);
    }

    public static TwitterRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TwitterRemoteDataSource();
        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<User> getUserInfo(final long userID) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                try {
                    Timber.d(String.valueOf(userID));
                    User user = mTwitter.showUser(userID);
                    subscriber.onNext(user);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
