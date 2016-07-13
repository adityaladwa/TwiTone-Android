package com.ladwa.aditya.twitone.data;

import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;
import com.ladwa.aditya.twitone.util.Utility;

import rx.Observable;
import rx.functions.Func1;

/**
 * A Twitter Repository that provides both local and Remote Data store
 * Created by Aditya on 24-Jun-16.
 */
public class TwitterRepository implements TwitterDataStore {

    private static TwitterRepository INSTANCE = null;
    private final TwitterLocalDataStore mLocalDataStore;
    private final TwitterRemoteDataSource mRemoteDataStore;


    private TwitterRepository(@NonNull TwitterLocalDataStore local, @NonNull TwitterRemoteDataSource remote) {
        this.mLocalDataStore = local;
        this.mRemoteDataStore = remote;

    }

    public static TwitterRepository getInstance(TwitterLocalDataStore local, TwitterRemoteDataSource remote) {
        if (INSTANCE == null)
            INSTANCE = new TwitterRepository(local, remote);
        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<User> getUserInfo(final long userID) {

        return Observable
                .concat(mLocalDataStore.getUserInfo(userID).first(), mRemoteDataStore.getUserInfo(userID))
                .first(new Func1<User, Boolean>() {
                    @Override
                    public Boolean call(User user) {
                        return user != null && Utility.checkProfileDataValidity(user.getLastModified());
                    }
                });
    }
}
