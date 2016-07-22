package com.ladwa.aditya.twitone.trends;

import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.models.Trend;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A Presenter for Trends
 * Created by Aditya on 22-Jul-16.
 */
public class TrendsPresenter implements TrendsContract.Presenter {


    private TrendsContract.View mView;
    private TwitterRepository mTwitterRepository;

    public TrendsPresenter(TrendsContract.View mView, TwitterRepository mTwitterRepository) {
        this.mView = mView;
        this.mTwitterRepository = mTwitterRepository;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadTrends();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadTrends() {
        mTwitterRepository.getTrends().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Trend>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Loaded Trends");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(List<Trend> trendList) {
                        mView.loadedTrends(trendList);
                        Timber.d("Loaded trends =" + trendList.size());
                    }
                });
    }

    @Override
    public void refreshRemoteTrends() {

    }
}
