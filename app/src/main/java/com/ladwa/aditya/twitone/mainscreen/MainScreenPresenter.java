package com.ladwa.aditya.twitone.mainscreen;

import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.local.models.User;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Twitter;

/**
 * A presenter for MainScreen
 * Created by Aditya on 27-Jun-16.
 */
public class MainScreenPresenter implements MainScreenContract.Presenter {

    private MainScreenContract.View mView;
    private Boolean mLogin;
    private long mUserId;
    private Subscription loadUserSubscription;
    private Twitter mTwitter;
    TwitterRepository mTwitterRepository;


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
        if (!mLogin)
            mView.logout();
        loadUserInfo();
        loadTimeLine();
    }

    @Override
    public void unsubscribe() {
        if (loadUserSubscription != null && !loadUserSubscription.isUnsubscribed())
            loadUserSubscription.unsubscribe();
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


        mTwitterRepository.getTimeLine()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Tweet>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Loaded TimeLine");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<Tweet> tweetList) {
                        for (Tweet tweet : tweetList) {
                            Timber.d(tweet.getTweet());
                        }
                        mView.loadTimeline(tweetList);
                    }
                });
    }
}
