package com.ladwa.aditya.twitone.data;

import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.data.local.models.Trend;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;
import com.ladwa.aditya.twitone.util.Utility;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

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

    public TwitterRemoteDataSource getmRemoteDataStore() {
        return mRemoteDataStore;
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

    @Override
    public Observable<List<Tweet>> getTimeLine(long sinceId) {
        return Observable
                .concat(mLocalDataStore.getTimeLine(sinceId).first(), mRemoteDataStore.getTimeLine(sinceId))
                .first(new Func1<List<Tweet>, Boolean>() {
                    @Override
                    public Boolean call(List<Tweet> tweetList) {
                        if (tweetList == null) {
                            Timber.d("Null");
                            return false;
                        } else {
                            if (tweetList.size() == 0)
                                return false;
                            else {
                                return true;
                            }
                        }
                    }
                });

        //    Send Only the local data
        //   return mLocalDataStore.getTimeLine();

    }

    @Override
    public Observable<List<Interaction>> getInteraction(long sinceId) {
        return Observable
                .concat(mLocalDataStore.getInteraction(sinceId).first(), mRemoteDataStore.getInteraction(sinceId))
                .first(new Func1<List<Interaction>, Boolean>() {
                    @Override
                    public Boolean call(List<Interaction> interactionList) {
                        if (interactionList == null) {
                            Timber.d("Null");
                            return false;
                        } else {
                            if (interactionList.size() == 0)
                                return false;
                            else {
                                return true;
                            }
                        }
                    }
                });
    }

    @Override
    public Observable<List<DirectMessage>> getDirectMessage(long sinceId) {
        return Observable
                .concat(mLocalDataStore.getDirectMessage(sinceId).first(), mRemoteDataStore.getDirectMessage(sinceId))
                .first(new Func1<List<DirectMessage>, Boolean>() {
                    @Override
                    public Boolean call(List<DirectMessage> directMessageList) {
                        if (directMessageList == null) {
                            Timber.d("Null");
                            return false;
                        } else {
                            if (directMessageList.size() == 0)
                                return false;
                            else {
                                return true;
                            }
                        }
                    }
                });
//        return mRemoteDataStore.getDirectMessage(sinceId);
//        return mLocalDataStore.getDirectMessage(sinceId).first();
    }

    @Override
    public Observable<List<Trend>> getTrends() {
        return Observable
                .concat(mLocalDataStore.getTrends().first(), mRemoteDataStore.getTrends())
                .filter(new Func1<List<Trend>, Boolean>() {
                    @Override
                    public Boolean call(List<Trend> trendList) {
                        if (trendList == null) {
                            Timber.d("Null");
                            return false;
                        } else {
                            if (trendList.size() == 0)
                                return false;
                            else {
                                return true;
                            }
                        }
                    }
                });

//        return mRemoteDataStore.getTrends();
    }

    @Override
    public Observable<List<Trend>> getLocalTrends(double latitude, double longitude) {
        return mRemoteDataStore.getLocalTrends(latitude, longitude);

//        return Observable
//                .concat(mLocalDataStore.getLocalTrends(latitude, longitude).first(), mRemoteDataStore.getLocalTrends(latitude, longitude))
//                .filter(new Func1<List<Trend>, Boolean>() {
//                    @Override
//                    public Boolean call(List<Trend> trendList) {
//                        if (trendList == null) {
//                            Timber.d("Null");
//                            return false;
//                        } else {
//                            if (trendList.size() == 0)
//                                return false;
//                            else {
//                                return true;
//                            }
//                        }
//                    }
//                });
    }


}
