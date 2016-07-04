package com.ladwa.aditya.twitone.data;

import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;

import rx.Observable;
import rx.functions.Action1;
import twitter4j.User;

/**
 * A Twitter Repository that provides both local and Remote Data store
 * Created by Aditya on 24-Jun-16.
 */
public class TwitterRepository implements TwitterDataStore {

    private static TwitterRepository INSTANCE = null;
    private final TwitterLocalDataStore mLocalDataStore;
    private final TwitterDataStore mRemoteDataStore;


    private TwitterRepository(@NonNull TwitterLocalDataStore local, @NonNull TwitterDataStore remote) {
        this.mLocalDataStore = local;
        this.mRemoteDataStore = remote;

    }

    public static TwitterRepository getInstance(TwitterLocalDataStore local, TwitterDataStore remote) {
        if (INSTANCE == null)
            INSTANCE = new TwitterRepository(local, remote);
        return INSTANCE;

    }

    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<com.ladwa.aditya.twitone.data.local.models.User> getUserInfo(long userID) {
        Observable<com.ladwa.aditya.twitone.data.local.models.User> userObservable = Observable.concat(mLocalDataStore.getUserInfo(userID), mRemoteDataStore.getUserInfo(userID)
                .doOnNext(new Action1<com.ladwa.aditya.twitone.data.local.models.User>() {
                    @Override
                    public void call(com.ladwa.aditya.twitone.data.local.models.User user) {
                        TwitterLocalDataStore.saveUserInfo(user);
                    }
                })).first();
        return userObservable;
    }
}
