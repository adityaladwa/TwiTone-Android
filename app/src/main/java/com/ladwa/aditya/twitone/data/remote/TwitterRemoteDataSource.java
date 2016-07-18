package com.ladwa.aditya.twitone.data.remote;

import android.content.SharedPreferences;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterDataStore;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.util.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import timber.log.Timber;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
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
    long id;


    private static TwitterRemoteDataSource INSTANCE = null;

    private TwitterRemoteDataSource() {
        TwitoneApp.getTwitterComponent().inject(this);
        id = preferences.getLong(TwitoneApp.getInstance().getString(R.string.pref_userid), 0);
        String token = preferences.getString(TwitoneApp.getInstance().getString(R.string.pref_access_token), "");
        String secret = preferences.getString(TwitoneApp.getInstance().getString(R.string.pref_access_secret), "");
        AccessToken accessToken = new AccessToken(token, secret);
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
                    localUser.setLastModified(Utility.getDateTime());


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

    @Override
    public Observable<List<Tweet>> getTimeLine() {
        final List<Tweet> localTweet = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<Tweet>>() {
            @Override
            public void call(Subscriber<? super List<Tweet>> subscriber) {
                try {

                    Paging p = new Paging();
                    p.setCount(100);
                    ResponseList<Status> homeTimeline = mTwitter.getHomeTimeline(p);

                    for (Status status : homeTimeline) {
                        Tweet tweet = new Tweet();
                        tweet.setTweet(status.getText());
                        tweet.setId(status.getId());
                        tweet.setDateCreated(String.valueOf(status.getCreatedAt()));
                        tweet.setLastModified(Utility.getDateTime());
                        tweet.setProfileUrl(status.getUser().getOriginalProfileImageURL());
                        tweet.setScreenName(status.getUser().getScreenName());
                        tweet.setUserName(status.getUser().getName());
                        tweet.setFavCount(status.getFavoriteCount());
                        tweet.setRetweetCount(status.getRetweetCount());
                        tweet.setVerified(status.getUser().isVerified() ? 1 : 0);
                        tweet.setFav(status.isFavorited() ? 1 : 0);
                        tweet.setRetweet(status.isRetweetedByMe() ? 1 : 0);

                        localTweet.add(tweet);

                    }
                    subscriber.onNext(localTweet);


                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).doOnNext(new Action1<List<Tweet>>() {
            @Override
            public void call(List<Tweet> tweets) {
                TwitterLocalDataStore.saveTimeLine(tweets);
            }
        });
    }
}
