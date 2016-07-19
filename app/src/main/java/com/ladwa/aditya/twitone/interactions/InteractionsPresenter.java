package com.ladwa.aditya.twitone.interactions;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
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
            loadInteractions();


    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadInteractions() {

        mTwitterRepository.getInteraction(TwitterLocalDataStore.getLastInteractionId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Interaction>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Loaded Mention");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<Interaction> interactionList) {
                        Timber.d(String.valueOf(interactionList.size()));
                        mView.loadInteractions(interactionList);
                    }
                });


    }

    @Override
    public void refreshRemoteInteraction() {
        TwitterRemoteDataSource.getInstance().getInteraction(TwitterLocalDataStore.getLastInteractionId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Interaction>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                        loadInteractions();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                        mView.stopRefreshing();
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<Interaction> interactionList) {
                        Timber.d("Loaded TimeLine from remote =" + String.valueOf(interactionList.size()));

                    }
                });

    }

    @Override
    public void createFavourite(long id) {

    }

    @Override
    public void unFavourite(long id) {

    }

    @Override
    public void createRetweet(long id) {

    }
}
