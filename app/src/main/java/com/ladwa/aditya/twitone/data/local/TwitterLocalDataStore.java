package com.ladwa.aditya.twitone.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterDataStore;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.local.models.TweetStorIOContentResolverDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.TweetStorIOContentResolverGetResolver;
import com.ladwa.aditya.twitone.data.local.models.TweetStorIOContentResolverPutResolver;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverDeleteResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverGetResolver;
import com.ladwa.aditya.twitone.data.local.models.UserStorIOContentResolverPutResolver;
import com.pushtorefresh.storio.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.queries.Query;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.List;

import rx.Observable;

/**
 * A class that implements TwitterDataStore for Local Database
 * Created by Aditya on 04-Jul-16.
 */
public class TwitterLocalDataStore implements TwitterDataStore {
    private static TwitterLocalDataStore INSTANCE;
    private static TwitterDbHelper mTwitterDbHelper;
    private static SQLiteDatabase db;
    private static StorIOSQLite mStorIOSQLite;
    private static StorIOContentResolver mStorIOContentResolver;

    private TwitterLocalDataStore(@NonNull Context context) {
        mTwitterDbHelper = new TwitterDbHelper(context);
        db = mTwitterDbHelper.getWritableDatabase();


        mStorIOContentResolver = DefaultStorIOContentResolver.builder()
                .contentResolver(context.getContentResolver())
                .addTypeMapping(User.class, ContentResolverTypeMapping.<User>builder()
                        .putResolver(new UserStorIOContentResolverPutResolver())
                        .getResolver(new UserStorIOContentResolverGetResolver())
                        .deleteResolver(new UserStorIOContentResolverDeleteResolver())
                        .build())
                .addTypeMapping(Tweet.class, ContentResolverTypeMapping.<Tweet>builder()
                        .putResolver(new TweetStorIOContentResolverPutResolver())
                        .getResolver(new TweetStorIOContentResolverGetResolver())
                        .deleteResolver(new TweetStorIOContentResolverDeleteResolver())
                        .build())
                .build();
    }

    public static TwitterLocalDataStore getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = new TwitterLocalDataStore(context);
        return INSTANCE;
    }

    @Override
    public Observable<User> getUserInfo(long userID) {
        return mStorIOContentResolver.get()
                .object(User.class)
                .withQuery(Query.builder()
                        .uri(TwitterContract.User.CONTENT_URI + "/" + userID)
                        .build())
                .prepare()
                .asRxObservable();
    }

    @Override
    public Observable<List<Tweet>> getTimeLine(long sinceId) {
        return mStorIOContentResolver.get()
                .listOfObjects(Tweet.class)
                .withQuery(Query.builder().uri(TwitterContract.Tweet.CONTENT_URI)
                        .sortOrder(TwitterContract.Tweet.COLUMN_ID + " DESC")
                        .build())
                .prepare()
                .asRxObservable();
    }

    @Override
    public Observable<List<Interaction>> getInteraction(long sinceId) {
        return null;
    }

    public static long getLastTweetId() {
        Tweet tweet = mStorIOContentResolver.get().object(Tweet.class)
                .withQuery(Query.builder().uri(TwitterContract.Tweet.CONTENT_URI)
                        .sortOrder(TwitterContract.Tweet.COLUMN_ID + " DESC LIMIT 1")
                        .build())
                .prepare()
                .executeAsBlocking();

        if (tweet == null)
            return 1;
        else
            return tweet.getId();
    }

    public static void createFavourite(Tweet tweet) {
        mStorIOContentResolver.put().object(tweet).prepare().executeAsBlocking();

    }


    public static void createRetweet(Tweet tweet) {
        mStorIOContentResolver.put().object(tweet).prepare().executeAsBlocking();
    }

    public static void destoryFavourite(Tweet tweet) {
        mStorIOContentResolver.put().object(tweet).prepare().executeAsBlocking();
    }


    public static void saveUserInfo(User user) {
        mStorIOContentResolver.put().object(user).prepare().executeAsBlocking();

    }

    public static void saveTimeLine(List<Tweet> tweetList) {
        mStorIOContentResolver.put().objects(tweetList).prepare().executeAsBlocking();
    }


}
