package com.ladwa.aditya.twitone.messagecompose;

import android.content.Context;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A Presenter Class for Compose Message module
 * Created by Aditya on 31-Jul-16.
 */
public class MessageComposePresenter implements MessageComposeContract.Presenter {

    private MessageComposeContract.View mView;
    private TwitterRepository mTwitterRepository;
    private long mSenderId;
    private Context mContext;


    public MessageComposePresenter(MessageComposeContract.View mView, TwitterRepository mTwitterRepository, Context mContext, long senderid) {
        this.mView = mView;
        this.mTwitterRepository = mTwitterRepository;
        this.mContext = mContext;
        this.mSenderId = senderid;
        mView.setPresenter(this);
    }

    @Override
    public void getUserDirectMessage() {
        TwitterLocalDataStore.getDirectMessageOfUser(mSenderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DirectMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
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
        TwitterRemoteDataSource.getInstance().sendDirectMessage(recipentId, message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<DirectMessage>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Message sent");
                        getUserDirectMessage();
                        mView.clearEditText();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
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
