package com.ladwa.aditya.twitone.interactions;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.ladwa.aditya.twitone.util.Utility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;


/**
 * A placeholder fragment containing a simple view.
 */
public class InteractionsFragment extends Fragment implements InteractionsContract.View,
        ConnectionReceiver.ConnectionReceiverListener {

    @Inject
    SharedPreferences preferences;
    @Inject
    Twitter mTwitter;
    @Inject
    TwitterRepository repository;

    @BindView(R.id.recyclerview_interaction)
    RecyclerView recyclerView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private Unbinder unbinder;
    private InteractionsContract.Presenter mPresenter;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private boolean internet;
    private boolean tablet;
    private boolean mLogin;
    private int orientation;


    public InteractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactions, container, false);
        unbinder = ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);

        //Check if tablet or phone
        tablet = Utility.isTablet(getActivity());

        //Check internet connection
        internet = ConnectionReceiver.isConnected();

        //Shared Preferences
        mLogin = preferences.getBoolean(getString(R.string.pref_login), false);
        long id = preferences.getLong(getString(R.string.pref_userid), 0);
        String token = preferences.getString(getString(R.string.pref_access_token), "");
        String secret = preferences.getString(getString(R.string.pref_access_secret), "");

        //Create instance of presenter
        AccessToken accessToken = new AccessToken(token, secret);
        mTwitter.setOAuthAccessToken(accessToken);
        new InteractionsPresenter(this, mLogin, id, mTwitter, repository);

        //Check configuration
        Configuration config = getActivity().getResources().getConfiguration();
        orientation = config.orientation;

        return view;
    }

    @Override
    public void setPresenter(InteractionsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        TwitoneApp.setConnectionListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        internet = isConnected;
    }
}
