package com.ladwa.aditya.twitone.ui.base;

import rx.Observable;
import rx.Single;

public class BasePresenter<T extends MvpView> implements MvpPresenter<T> {

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call MvpPresenter.attachView(MvpView) before" +
                    " requesting data to the MvpPresenter");
        }
    }

    protected static class DataResult<T> {

        private T mData;
        private Throwable mError;

        public DataResult(T data) {
            mData = data;
        }

        public DataResult(Throwable error) {
            mError = error;
        }

        public Single<T> toSingle() {
            if (mError != null) {
                return Single.error(mError);
            }
            return Single.just(mData);
        }

        public Observable<T> toObservable() {
            if (mError != null) {
                return Observable.error(mError);
            }
            return Observable.just(mData);
        }
    }
}

