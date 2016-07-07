package com.ladwa.aditya.twitone.data.remote;

import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterDataStore;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
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
    public Observable<com.ladwa.aditya.twitone.data.local.models.User> getUserInfo(final long userID) {
        final com.ladwa.aditya.twitone.data.local.models.User localUser = new com.ladwa.aditya.twitone.data.local.models.User();
        return Observable.create(new Observable.OnSubscribe<com.ladwa.aditya.twitone.data.local.models.User>() {
            @Override
            public void call(Subscriber<? super com.ladwa.aditya.twitone.data.local.models.User> subscriber) {
                try {
                    Timber.d(String.valueOf(userID));
                    User user = mTwitter.showUser(userID);
                    localUser.setId(user.getId());
                    localUser.setName(user.getName());
                    localUser.setScreenName(user.getScreenName());
                    localUser.setProfileUrl(user.getOriginalProfileImageURL());
                    localUser.setBannerUrl(user.getProfileBannerMobileRetinaURL());

                    subscriber.onNext(localUser);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }

        }).doOnNext(new Action1<com.ladwa.aditya.twitone.data.local.models.User>() {
            @Override
            public void call(com.ladwa.aditya.twitone.data.local.models.User user) {
                TwitterLocalDataStore.saveUserInfo(user);
            }
        });
    }
}
