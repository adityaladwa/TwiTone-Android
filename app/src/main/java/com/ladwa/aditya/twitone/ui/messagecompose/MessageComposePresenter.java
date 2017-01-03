package com.ladwa.aditya.twitone.ui.messagecompose;

import android.content.Context;
import android.util.Log;

import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A Presenter Class for Compose Message module
 * Created by Aditya on 31-Jul-16.
 */
public class MessageComposePresenter implements MessageComposeContract.Presenter {

    private static final String TAG = MessageComposePresenter.class.getSimpleName();
    private MessageComposeContract.View mView;
    private long mSenderId;
    private Context mContext;

    @Inject
    TwitterLocalDataStore mTwitterLocalDataStore;

    @Inject
    TwitterRemoteDataSource mTwitterRemoteDataSource;


    public MessageComposePresenter(MessageComposeContract.View mView, Context mContext, long senderid) {
        this.mView = mView;
        this.mContext = mContext;
        this.mSenderId = senderid;
        mView.setPresenter(this);
        TwitoneApp.getTwitterComponent().inject(this);
    }

    @Override
    public void getUserDirectMessage() {
        mTwitterLocalDataStore.getDirectMessageOfUser(mSenderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DirectMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<DirectMessage> directMessageList) {
                        mView.loadUserDirectMessage(directMessageList);
                        mView.stopRefreshing();
                    }
                });

    }

    @Override
    public void sendDirectMessage(long recipentId, String message) {
        mTwitterRemoteDataSource.sendDirectMessage(recipentId, message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<DirectMessage>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("Message sent");
                        getUserDirectMessage();
                        mView.clearEditText();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(DirectMessage directMessage) {

                    }
                });
    }

    @Override
    public void subscribe() {
        getUserDirectMessage();
    }

    @Override
    public void unsubscribe() {

    }
}
