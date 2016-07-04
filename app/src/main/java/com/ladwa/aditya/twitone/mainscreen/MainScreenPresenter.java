package com.ladwa.aditya.twitone.mainscreen;

import android.support.annotation.NonNull;

import com.ladwa.aditya.twitone.data.TwitterRepository;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Twitter;
import twitter4j.User;

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
    }

    @Override
    public void unsubscribe() {
        if (loadUserSubscription != null && !loadUserSubscription.isUnsubscribed())
            loadUserSubscription.unsubscribe();
    }

    @Override
    public void loadUserInfo() {

//        loadUserSubscription = Observable.create(new Observable.OnSubscribe<User>() {
//            @Override
//            public void call(Subscriber<? super User> subscriber) {
//                try {
//                    Timber.d(String.valueOf(mUserId));
//                    User user = mTwitter.showUser(mUserId);
//                    subscriber.onNext(user);
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                } finally {
//                    subscriber.onCompleted();
//                }
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<User>() {
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("Completed set user");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.e(e, "Error :" + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//                        mView.loadedUser(user);
//                        Timber.d(user.getName());
//                    }
//                }


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
}
