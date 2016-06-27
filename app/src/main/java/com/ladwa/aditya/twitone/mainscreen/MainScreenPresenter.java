package com.ladwa.aditya.twitone.mainscreen;

import android.support.annotation.NonNull;

/**
 * Created by Aditya on 27-Jun-16.
 */
public class MainScreenPresenter implements MainScreenContract.Presenter {

    private MainScreenContract.View mView;
    private Boolean mLogin;

    public MainScreenPresenter(@NonNull MainScreenContract.View mView, @NonNull Boolean mLogin) {
        this.mView = mView;
        this.mLogin = mLogin;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (!mLogin)
            mView.logout();
    }

    @Override
    public void unsubscribe() {

    }
}
