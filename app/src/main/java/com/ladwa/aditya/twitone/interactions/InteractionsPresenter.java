package com.ladwa.aditya.twitone.interactions;

import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    @Inject
    TwitterLocalDataStore mTwitterLocalDataStore;


    public InteractionsPresenter(InteractionsContract.View mView, Boolean mLogin, long mUserId, Twitter mTwitter, TwitterRepository repository) {
        this.mView = mView;
        this.mLogin = mLogin;
        this.mUserId = mUserId;
        this.mTwitter = mTwitter;
        this.mTwitterRepository = repository;
        mView.setPresenter(this);
        TwitoneApp.getTwitterComponent().inject(this);
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

        mTwitterRepository.getInteraction(mTwitterLocalDataStore.getLastInteractionId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Interaction>>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("Loaded Mention");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<Interaction> interactionList) {
//                        Timber.d(String.valueOf(interactionList.size()));
                        mView.loadInteractions(interactionList);
                    }
                });


    }

    @Override
    public void refreshRemoteInteraction() {
        mTwitterRepository.getInteraction(mTwitterLocalDataStore.getLastInteractionId())
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
//                        Timber.e(e, "Error :" + e.toString());
                        mView.stopRefreshing();
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<Interaction> interactionList) {
//                        Timber.d("Loaded TimeLine from remote =" + String.valueOf(interactionList.size()));

                    }
                });

    }

    @Override
    public void createFavourite(long id) {
        mTwitterRepository.getmRemoteDataStore().createFavouriteInteraction(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Interaction>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("Created Favourite");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
//                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(Interaction interaction) {
                        mView.createdFavouriteCallback(interaction);
                    }
                });
    }

    @Override
    public void unFavourite(long id) {
        mTwitterRepository.getmRemoteDataStore().destoryFavouriteInteraction(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Interaction>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("Destoryed Favourite");

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
//                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(Interaction interaction) {
                        mView.destroyFavouriteCallback(interaction);
                    }
                });
    }

    @Override
    public void createRetweet(long id) {
        mTwitterRepository.getmRemoteDataStore().createRetweetInteraction(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Interaction>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("Created Retweet");

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
//                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(Interaction interaction) {
                        mView.createRetweetCallback(interaction);

                    }
                });


    }
}
