package com.ladwa.aditya.twitone.interactions;

import com.ladwa.aditya.twitone.data.TwitterRepository;

import twitter4j.Twitter;

/**
 * A Presenter Class for Interactions
 * Created by Aditya on 19-Jul-16.
 */
public class InteractionsPresenter implements InteractionsContract.Presenter {

    private InteractionsContract.View mView;
    private Boolean mLogin;
    private long mUserId;
    private Twitter mTwitter;
    private TwitterRepository mTwitterRepository;


    public InteractionsPresenter(InteractionsContract.View mView, Boolean mLogin, long mUserId, Twitter mTwitter, TwitterRepository repository) {
        this.mView = mView;
        this.mLogin = mLogin;
        this.mUserId = mUserId;
        this.mTwitter = mTwitter;
        this.mTwitterRepository = repository;
        mView.setPresenter(this);

    }

    @Override
    public void subscribe() {
        if (mLogin)
            getUserInteractions();


    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getUserInteractions() {

    }
}
