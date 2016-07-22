package com.ladwa.aditya.twitone.interactions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.adapter.InteractionAdapter;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.ladwa.aditya.twitone.util.Utility;
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
 * A placeholder fragment containing a simple view.
 */
public class InteractionsFragment extends Fragment implements InteractionsContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        InteractionAdapter.InteractionClickListener,
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

    private List<Interaction> mInteractions;
    private InteractionAdapter mInteractionAdapter;

    private SharedPreferences.Editor editor;


    private boolean internet;
    private boolean tablet;
    private boolean mLogin;
    private int orientation;
    private int finalPos;


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
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
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

        mInteractions = new ArrayList<>();
        mInteractionAdapter = new InteractionAdapter(mInteractions, getActivity());
        mInteractionAdapter.setmInteractionClickListener(this);
        recyclerView.setAdapter(mInteractionAdapter);
        swipeContainer.setOnRefreshListener(this);


        if (tablet)
            Timber.d("Tablet");
        else
            Timber.d("Phone");

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        RefWatcher refWatcher = TwitoneApp.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        internet = isConnected;
    }

    @Override
    public void loadInteractions(List<Interaction> interactionList) {
        int oldSize = mInteractions.size();
        int newSize = interactionList.size();
        int saveScrollPos = preferences.getInt(getString(R.string.pref_scroll_pos_interaction), 0);


        if (saveScrollPos > 0) {
            finalPos = saveScrollPos;
        } else {
            finalPos = newSize - oldSize;
        }

        Timber.d("Final pos = " + String.valueOf(finalPos));
        mInteractions.clear();
        mInteractions.addAll(interactionList);
        mInteractionAdapter.notifyDataSetChanged();
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
    public void stopRefreshing() {
        if (swipeContainer != null)
            swipeContainer.setRefreshing(false);
    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_occured, Snackbar.LENGTH_LONG)
                .show();
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
        editor.putInt(getActivity().getResources().getString(R.string.pref_scroll_pos_interaction), pos);
        editor.apply();
    }

    @Override
    public void onRefresh() {
        if (internet) {
            mPresenter.refreshRemoteInteraction();
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
            if (swipeContainer != null)
                swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void createdFavouriteCallback(Interaction interaction) {
        Snackbar.make(recyclerView, R.string.favourite, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadInteractions();
    }

    @Override
    public void destroyFavouriteCallback(Interaction interaction) {
        Snackbar.make(recyclerView, R.string.unfavourite, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadInteractions();
    }

    @Override
    public void createRetweetCallback(Interaction interaction) {
        Snackbar.make(recyclerView, R.string.retweet, Snackbar.LENGTH_LONG)
                .show();
        mPresenter.loadInteractions();
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClickedFavourite(View view, int position) {
        if (internet) {
            if (mInteractions.get(position).getFav() == 0) {
                mPresenter.createFavourite(mInteractions.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_heart).color(Color.RED));
            } else {
                mPresenter.unFavourite(mInteractions.get(position).getId());
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
            if (mInteractions.get(position).getRetweet() == 0) {
                mPresenter.createRetweet(mInteractions.get(position).getId());
                ((ImageView) view).setImageDrawable(new IconicsDrawable(getActivity()).icon(FontAwesome.Icon.faw_retweet).color(Color.BLUE));
            }
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onLongClick(View view, int position) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(mInteractions.get(position).getUserName(), mInteractions.get(position).getTweet());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }
}
