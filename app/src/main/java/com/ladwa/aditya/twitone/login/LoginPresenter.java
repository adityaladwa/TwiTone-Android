package com.ladwa.aditya.twitone.login;

import android.support.annotation.NonNull;

/**
 * Created by Aditya on 25-Jun-16.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mView;

    public LoginPresenter(@NonNull LoginContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void login() {
        mView.onSuccess();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
