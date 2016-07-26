package com.ladwa.aditya.twitone.mainscreen;

import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Twitter;

/**
 * A presenter for MainScreen
 * Created by Aditya on 27-Jun-16.
 */
public class MainScreenPresenter implements MainScreenContract.Presenter {

    //TODO Add subscriber variable to fix memory leaks and nullify the in unsubscribe method
    private MainScreenContract.View mView;
    private Boolean mLogin;
    private long mUserId;
    private Twitter mTwitter;
    private TwitterRepository mTwitterRepository;


    public MainScreenPresenter(@NonNull MainScreenContract.View mView, @NonNull Boolean mLogin, long userId, Twitter twitter, TwitterRepository repository) {
        this.mView = mView;
        this.mLogin = mLogin;
        this.mUserId = userId;
        this.mTwitter = twitter;
        this.mTwitterRepository = repository;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (!mLogin) {
            mView.logout();
        } else {
            loadUserInfo();
            loadTimeLine();
        }
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadUserInfo() {

        mTwitterRepository.getUserInfo(mUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Completed set user");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(User user) {
                        mView.loadedUser(user);
                    }
                });

    }

    @Override
    public void loadTimeLine() {
        mTwitterRepository.getTimeLine(TwitterLocalDataStore.getLastTweetId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Tweet>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Loaded TimeLine");
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                        mView.stopRefreshing();
                    }

                    @Override
                    public void onNext(List<Tweet> tweetList) {
                        Timber.d(String.valueOf(tweetList.size()));
                        mView.loadTimeline(tweetList);
                    }
                });
    }

    @Override
    public void refreshRemoteTimeline() {
        TwitterRemoteDataSource.getInstance().getTimeLine(TwitterLocalDataStore.getLastTweetId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Tweet>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                        loadTimeLine();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                        mView.stopRefreshing();
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<Tweet> tweetList) {
                        Timber.d("Loaded TimeLine from remote =" + String.valueOf(tweetList.size()));
                        mView.showNotification(tweetList.size());
                    }
                });
    }

    @Override
    public void createFavourite(long id) {
        mTwitterRepository.getmRemoteDataStore().createFavourite(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Tweet>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Created Favourite");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(Tweet tweet) {
                        mView.createdFavouriteCallback(tweet);
                    }
                });
    }

    @Override
    public void unFavourite(long id) {
        mTwitterRepository.getmRemoteDataStore().destoryFavourite(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Tweet>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Destoryed Favourite");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(Tweet tweet) {
                        mView.destroyFavouriteCallback(tweet);
                    }
                });
    }

    @Override
    public void createRetweet(long id) {
        mTwitterRepository.getmRemoteDataStore().createRetweet(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Tweet>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Created Retweet");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(Tweet tweet) {
                        mView.createRetweetCallback(tweet);
                    }
                });
    }
}
