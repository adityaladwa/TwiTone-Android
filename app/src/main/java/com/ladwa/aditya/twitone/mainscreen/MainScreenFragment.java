package com.ladwa.aditya.twitone.mainscreen;

import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import com.ladwa.aditya.twitone.imageviewer.ImageViewer;
import com.ladwa.aditya.twitone.login.LoginActivity;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.ladwa.aditya.twitone.util.Utility;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        TimelineAdapter.TimeLineClickListener,
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

    private FloatingActionButton mActionButton;

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

        mActionButton = (FloatingActionButton) getActivity().findViewById(R.id.tweet_fab);

        //SetupDrawer
        String screenName = preferences.getString(getString(R.string.pref_screen_name), "");
        mDrawerCallback.setProfile(screenName);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        ConnectionReceiver.setConnectionReceiverListener(this);
        //Check internet connection
        internet = ConnectionReceiver.isConnected();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
        saveScrollPosition();
        ConnectionReceiver.destoryInstance();
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
            linearLayoutManager.scrollToPosition(finalPos);
            linearLayoutManager.scrollToPosition(finalPos);
        } else if (!tablet && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            staggeredGridLayoutManager.scrollToPosition(finalPos);
            staggeredGridLayoutManager.scrollToPosition(finalPos);
        } else if (tablet) {
            staggeredGridLayoutManager.scrollToPosition(finalPos);
            staggeredGridLayoutManager.scrollToPosition(finalPos);
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


        if (getActivity() != null) {

            editor = preferences.edit();
            editor.putInt(getActivity().getResources().getString(R.string.pref_scroll_pos), pos);
            editor.apply();
        }
    }

    @Override
    public void stopRefreshing() {
        if (swipeContainer != null)
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
    }

    @Override
    public void showRefreshing() {
        if (swipeContainer != null)
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);
                }
            });
    }

    @Override
    public void showNotification(int tweets) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_user_type_verified)
                        .setContentTitle(tweets + getActivity().getString(R.string.new_tweets))
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.RED, 3000, 3000)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentText(mTweets.get(0).getTweet());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(tweets + getActivity().getString(R.string.new_tweets));
        for (int i = 0; i < 5; i++) {
            inboxStyle.addLine("@" + mTweets.get(i).getScreenName() + " : " + mTweets.get(i).getTweet());
        }

        mBuilder.setStyle(inboxStyle);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getActivity(),
                        0,
                        new Intent(getActivity(), MainScreen.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat.from(getActivity()).notify(10, mBuilder.build());
    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_occured, Snackbar.LENGTH_LONG)
                .show();

    }

    @Override
    public void createdFavouriteCallback(Tweet tweet) {
        Snackbar.make(recyclerView, R.string.favourite, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadTimeLine();
    }

    @Override
    public void destroyFavouriteCallback(Tweet tweet) {
        Snackbar.make(recyclerView, R.string.unfavourite, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadTimeLine();
    }

    @Override
    public void createRetweetCallback(Tweet tweet) {
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
        //Start detail tweet view activity
    }

    @Override
    public void onClickedReplay(View view, int position) {
        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        Matcher split = mentionPattern.matcher(mTweets.get(position).getTweet());

        String mention = "";
        String concat = "";

        while (split.find()) {

            for (int i = 0; i < split.groupCount(); i++) {
                Timber.d(split.group(i));
                concat += split.group(i) + " ";
            }

        }
        Timber.d(concat);
        String inReplay = "@" + mTweets.get(position).getScreenName() + " " + concat;

        Intent intent = new Intent(getActivity(), com.ladwa.aditya.twitone.tweet.Tweet.class);
        intent.putExtra(getActivity().getString(R.string.extra_id), mTweets.get(position).getId());
        intent.putExtra(getActivity().getString(R.string.extra_replay), inReplay);
        intent.putExtra(getActivity().getString(R.string.extra_is_replay), true);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), mActionButton, mActionButton.getTransitionName());

        startActivity(intent, options.toBundle());

    }

    @Override
    public void onClickedFavourite(View view, int position) {
        if (internet) {
            if (mTweets.get(position).getFav() == 0) {
                mPresenter.createFavourite(mTweets.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_heart).color(Color.RED));
            } else {
                mPresenter.unFavourite(mTweets.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_heart).color(Color.GRAY));

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
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_retweet).color(Color.GREEN));
            }
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onLongClick(View view, int position) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(mTweets.get(position).getUserName(), mTweets.get(position).getTweet());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickMedia(View view, int position) {
        //Dummy
        Intent intent = new Intent(getActivity(), ImageViewer.class);
        intent.putExtra(getActivity().getString(R.string.extra_url), mTweets.get(position).getMediaUrl());
        startActivity(intent);
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
