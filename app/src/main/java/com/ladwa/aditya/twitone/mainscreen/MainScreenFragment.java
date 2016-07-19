package com.ladwa.aditya.twitone.mainscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.adapter.TimelineAdapter;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.local.models.User;
import com.ladwa.aditya.twitone.login.LoginActivity;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.ladwa.aditya.twitone.util.Utility;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;


/**
 * This is the Main Fragment that users will see once they open the app
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment
        implements MainScreenContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        TimelineAdapter.TimeLineClickListner,
        ConnectionReceiver.ConnectionReceiverListener {

    @Inject
    SharedPreferences preferences;
    @Inject
    Twitter mTwitter;
    @Inject
    TwitterRepository repository;

    @BindView(R.id.recyclerview_timeline)
    RecyclerView recyclerView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    private boolean internet;


    private boolean mLogin;
    private Unbinder unbinder;
    private MainScreenContract.Presenter mPresenter;
    private DrawerCallback mDrawerCallback;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TimelineAdapter mTimelineAdapter;
    private SharedPreferences.Editor editor;
    private int finalPos;
    private boolean tablet;
    private int orientation;

    private List<Tweet> mTweets;

    public MainScreenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
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
        new MainScreenPresenter(this, mLogin, id, mTwitter, repository);

        TwitterLocalDataStore.getInstance(getActivity());

        Configuration config = getActivity().getResources().getConfiguration();
        orientation = config.orientation;

        //Recycler view
        if (tablet && orientation == Configuration.ORIENTATION_PORTRAIT)
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        else if (tablet && orientation == Configuration.ORIENTATION_LANDSCAPE)
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        else if (!tablet && orientation == Configuration.ORIENTATION_LANDSCAPE)
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        else
            linearLayoutManager = new LinearLayoutManager(getActivity());


        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(false);

        if (!tablet && orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.setLayoutManager(linearLayoutManager);
        else if (!tablet && orientation == Configuration.ORIENTATION_LANDSCAPE)
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        else if (tablet)
            recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    saveScrollPosition();
                }
            }
        });

        mTweets = new ArrayList<>();
        mTimelineAdapter = new TimelineAdapter(mTweets, getActivity());
        mTimelineAdapter.setTimeLineClickListner(this);
        recyclerView.setAdapter(mTimelineAdapter);
        swipeContainer.setOnRefreshListener(this);


        if (tablet)
            Timber.d("Tablet");
        else
            Timber.d("Phone");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDrawerCallback = (DrawerCallback) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //SetupDrawer
        String screenName = preferences.getString(getString(R.string.pref_screen_name), "");
        mDrawerCallback.setProfile(screenName);

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
        saveScrollPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TwitoneApp.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.pref_login), false);
        editor.apply();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            CookieManager.getInstance().removeAllCookie();
        } else {
            CookieManager.getInstance().removeAllCookies(null);
        }
        mTwitter.setOAuthAccessToken(null);
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void loadedUser(User user) {
        mDrawerCallback.updateProfile(user);
    }

    @Override
    public void loadTimeline(List<Tweet> tweetList) {
        int oldSize = mTweets.size();
        int newSize = tweetList.size();
        int saveScrollPos = preferences.getInt("Scroll_pos", 0);


        if (saveScrollPos > 0) {
            finalPos = saveScrollPos;
        } else {
            finalPos = newSize - oldSize;
        }

        Timber.d("Final pos = " + String.valueOf(finalPos));
        mTweets.clear();
        mTweets.addAll(tweetList);
        mTimelineAdapter.notifyDataSetChanged();
        setScrollPos();
    }

    @Override
    public void setScrollPos() {

        if (!tablet && orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager.scrollToPosition(finalPos + 10);
            linearLayoutManager.scrollToPosition(finalPos);
        } else if (!tablet && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            staggeredGridLayoutManager.scrollToPosition(finalPos + 10);
            staggeredGridLayoutManager.scrollToPosition(finalPos);
        } else if (tablet) {
            staggeredGridLayoutManager.scrollToPosition(finalPos);
            staggeredGridLayoutManager.scrollToPosition(finalPos + 10);
        }
    }

    @Override
    public void saveScrollPosition() {
        int pos = 0;
        int[] position = new int[2];

        if (!tablet && orientation == Configuration.ORIENTATION_PORTRAIT)
            pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        else if (!tablet && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            pos = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(position)[0];
            Timber.d("Scroll pos= " + position[0]);
        } else if (tablet && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            position = new int[3];
            pos = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(position)[0];
            Timber.d("Scroll pos= " + position[0]);
        } else if (tablet && orientation == Configuration.ORIENTATION_PORTRAIT) {
            pos = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(position)[0];
            Timber.d("Scroll pos asd= " + position[0]);
        }


        editor = preferences.edit();
        editor.putInt("Scroll_pos", pos);
        editor.apply();
    }

    @Override
    public void stopRefreshing() {
        if (swipeContainer != null)
            swipeContainer.setRefreshing(false);
    }

    @Override
    public void showError() {
//        Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
        Snackbar.make(recyclerView, R.string.error_occured, Snackbar.LENGTH_LONG)
                .show();

    }

    @Override
    public void createdFavouriteCallback(Tweet tweet) {
//        Toast.makeText(getActivity(), "Favourite", Toast.LENGTH_SHORT).show();
        Snackbar.make(recyclerView, R.string.favourite, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadTimeLine();
    }

    @Override
    public void destroyFavouriteCallback(Tweet tweet) {
//        Toast.makeText(getActivity(), "Unfavourite", Toast.LENGTH_SHORT).show();
        Snackbar.make(recyclerView, R.string.unfavourite, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadTimeLine();
    }

    @Override
    public void createRetweetCallback(Tweet tweet) {
//        Toast.makeText(getActivity(), "Retweeted", Toast.LENGTH_SHORT).show();
        Snackbar.make(recyclerView, R.string.retweet, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadTimeLine();
    }


    @Override
    public void setPresenter(MainScreenContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        if (internet) {
            mPresenter.refreshRemoteTimeline();
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
            if (swipeContainer != null)
                swipeContainer.setRefreshing(false);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickedFavourite(View view, int position) {
        if (internet) {
            if (mTweets.get(position).getFav() == 0) {
                mPresenter.createFavourite(mTweets.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_heart).color(Color.YELLOW));
            } else {
                mPresenter.unFavourite(mTweets.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_heart).color(Color.BLACK));

            }
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
        }


    }

    @Override
    public void onClickedRetweet(View view, int position) {
        if (internet) {
            if (mTweets.get(position).getRetweet() == 0) {
                mPresenter.createRetweet(mTweets.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_retweet).color(Color.BLUE));
            }
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Timber.d("Internet changed");
        internet = isConnected;
    }


    public interface DrawerCallback {
        void setProfile(String screenName);

        void updateProfile(User user);
    }
}
