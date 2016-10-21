package com.ladwa.aditya.twitone.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ladwa.aditya.twitone.util.Constants;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import twitter4j.AsyncTwitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * LoginPersenter implements {@link com.ladwa.aditya.twitone.login.LoginContract.Presenter}
 * Created by Aditya on 25-Jun-16.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final static String TAG = LoginPresenter.class.getSimpleName();

    private final LoginContract.View mView;
    private final AsyncTwitter mTwitter;
    private RequestToken mRequestToken;
    private Subscription requestSubscription, accessSubscription;


    public LoginPresenter(@NonNull LoginContract.View view, @NonNull AsyncTwitter twitter) {
        mView = view;
        mTwitter = twitter;
        mView.setPresenter(this);
    }


    @Override
    public void login() {
        requestSubscription = getRequestTokenObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RequestToken>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError("Cant recieve Request token! Try again after 2 minutes");
                    }

                    @Override
                    public void onNext(RequestToken requestToken) {
                        mView.startOauthIntent(requestToken);
                    }
                });

    }


    @Override
    public void getAccessToken(final String verifier) {

        accessSubscription = getAccessTokenObservable(verifier).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        mView.onSuccess("Login Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError("Error recieving Access Token! Try again After sometime");
                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
                        mView.saveAccessTokenAnd(accessToken);
                        Log.d(TAG, "Access token is :" + accessToken.getToken() + accessToken.getScreenName());
                    }
                });


    }

    @Override
    public Observable<RequestToken> getRequestTokenObservable() {
        return Observable.create(new Observable.OnSubscribe<RequestToken>() {
            @Override
            public void call(Subscriber<? super RequestToken> subscriber) {
                try {
                    mRequestToken = mTwitter.getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
                    subscriber.onNext(mRequestToken);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<AccessToken> getAccessTokenObservable(final String verifier) {
        return Observable.create(new Observable.OnSubscribe<AccessToken>() {
            @Override
            public void call(Subscriber<? super AccessToken> subscriber) {
                try {
                    AccessToken accessToken = mTwitter.getOAuthAccessToken(mRequestToken, verifier);
                    subscriber.onNext(accessToken);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }


    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        if (requestSubscription != null && !requestSubscription.isUnsubscribed())
            requestSubscription.unsubscribe();
        if (accessSubscription != null && !accessSubscription.isUnsubscribed())
            accessSubscription.unsubscribe();
    }
}
