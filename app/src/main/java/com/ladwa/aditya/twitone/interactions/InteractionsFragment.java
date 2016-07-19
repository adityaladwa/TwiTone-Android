package com.ladwa.aditya.twitone.interactions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;

import timber.log.Timber;


/**
 * A placeholder fragment containing a simple view.
 */
public class InteractionsFragment extends Fragment implements InteractionsContract.View {

    private InteractionsContract.Presenter mPresenter;

    public InteractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_interactions, container, false);

        Timber.d(String.valueOf(TwitterLocalDataStore.getLastTweetId()));
        return inflate;
    }

    @Override
    public void setPresenter(InteractionsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
