package com.ladwa.aditya.twitone.ui.message;

import android.content.Context;

import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import twitter4j.Twitter;

/**
 * A Presenter class for Direct Message
 * Created by Aditya on 20-Jul-16.
 */
public class MessagePresenter implements MessageContract.Presenter {

    private MessageContract.View mView;
    private Boolean mLogin;
    private long mUserId;
    private Twitter mTwitter;
    @Inject
    TwitterRepository mTwitterRepository;

    @Inject
    TwitterRemoteDataSource mTwitterRemoteDataSource;

    @Inject
    TwitterLocalDataStore mTwitterLocalDataStore;
    private Context mContext;

    public MessagePresenter(MessageContract.View mView, Boolean mLogin, long mUserId, Twitter mTwitter, Context context) {
        this.mView = mView;
        this.mLogin = mLogin;
        this.mUserId = mUserId;
        this.mTwitter = mTwitter;
        this.mContext = context;
        mView.setPresenter(this);
        TwitoneApp.getTwitterComponent().inject(this);
    }

    @Override
    public void loadDirectMessage() {
        mTwitterRepository.getDirectMessage(mTwitterLocalDataStore.getLastDirectMessageId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DirectMessage>>() {
                    @Override
                    public void onCompleted() {
                        getlocalDm();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<DirectMessage> directMessageList) {
//                        Timber.d(String.valueOf(directMessageList.size()));
                    }
                });


    }

    private void getlocalDm() {
        mTwitterLocalDataStore.getDirectMessage(mTwitterLocalDataStore.getLastDirectMessageId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DirectMessage>>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("loaded from local data store ");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<DirectMessage> directMessageList) {
                        mView.loadDirectMessage(directMessageList);
                        mView.stopRefreshing();
                    }
                });
    }

    @Override
    public void refreshRemoteDirectMessage() {
        mTwitterRemoteDataSource.getDirectMessage(mTwitterLocalDataStore.getLastDirectMessageId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DirectMessage>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefreshing();
                        loadDirectMessage();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.e(e, "Error :" + e.toString());
                        mView.stopRefreshing();
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<DirectMessage> directMessageList) {
//                        Timber.d("Loaded TimeLine from remote =" + String.valueOf(directMessageList.size()));

                    }
                });

    }


    @Override
    public void subscribe() {
        loadDirectMessage();
    }

    @Override
    public void unsubscribe() {

    }
}
