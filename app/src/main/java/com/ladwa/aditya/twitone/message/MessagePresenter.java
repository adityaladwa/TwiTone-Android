package com.ladwa.aditya.twitone.message;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
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
    private TwitterRepository mTwitterRepository;

    public MessagePresenter(MessageContract.View mView, Boolean mLogin, long mUserId, Twitter mTwitter, TwitterRepository mTwitterRepository) {
        this.mView = mView;
        this.mLogin = mLogin;
        this.mUserId = mUserId;
        this.mTwitter = mTwitter;
        this.mTwitterRepository = mTwitterRepository;
        mView.setPresenter(this);
    }

    @Override
    public void loadDirectMessage() {

        mTwitterRepository.getDirectMessage(TwitterLocalDataStore.getLastDirectMessageId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<DirectMessage>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Loaded TimeLine");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error :" + e.toString());
                    }

                    @Override
                    public void onNext(List<DirectMessage> directMessageList) {
                        Timber.d(String.valueOf(directMessageList.size()));
                        mView.loadDirectMessage(directMessageList);
                    }
                });

    }

    @Override
    public void refreshRemoteDirectMessage() {
        TwitterRemoteDataSource.getInstance().getDirectMessage(TwitterLocalDataStore.getLastDirectMessageId())
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
                        Timber.e(e, "Error :" + e.toString());
                        mView.stopRefreshing();
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<DirectMessage> directMessageList) {
                        Timber.d("Loaded TimeLine from remote =" + String.valueOf(directMessageList.size()));

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
