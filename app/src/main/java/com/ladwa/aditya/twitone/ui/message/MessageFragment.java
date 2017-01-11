package com.ladwa.aditya.twitone.ui.message;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.ui.adapter.DirectMessageAdapter;
import com.ladwa.aditya.twitone.ui.messagecompose.MessageCompose;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;


/**
 * A placeholder fragment containing a simple view.
 */
public class MessageFragment extends Fragment implements MessageContract.View,
        ConnectionReceiver.ConnectionReceiverListener,
        SwipeRefreshLayout.OnRefreshListener,
        DirectMessageAdapter.DirectMessageClickListener {

    @Inject SharedPreferences preferences;
    @Inject Twitter mTwitter;
    @Inject TwitterRepository repository;

    @BindView(R.id.recyclerview_directmessage) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private Unbinder unbinder;
    private MessageContract.Presenter mPresenter;

    private LinearLayoutManager linearLayoutManager;

    private List<DirectMessage> mDirectMessages;
    private DirectMessageAdapter mDirectMessageAdapter;

    private SharedPreferences.Editor editor;


    private boolean internet;
    private boolean mLogin;

    public MessageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        unbinder = ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);

        //Shared Preferences
        mLogin = preferences.getBoolean(getString(R.string.pref_login), false);
        long id = preferences.getLong(getString(R.string.pref_userid), 0);
        String token = preferences.getString(getString(R.string.pref_access_token), "");
        String secret = preferences.getString(getString(R.string.pref_access_secret), "");

        //Create instance of presenter
        AccessToken accessToken = new AccessToken(token, secret);
        mTwitter.setOAuthAccessToken(accessToken);
        new MessagePresenter(this, mLogin, id, mTwitter, getActivity());


        linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);


        mDirectMessages = new ArrayList<>();
        mDirectMessageAdapter = new DirectMessageAdapter(mDirectMessages, getActivity());
        mDirectMessageAdapter.setMessageClickListener(this);
        recyclerView.setAdapter(mDirectMessageAdapter);
        swipeContainer.setOnRefreshListener(this);


        return view;
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
        ConnectionReceiver.destoryInstance();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        if (internet) {
            mPresenter.refreshRemoteDirectMessage();
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
            if (swipeContainer != null)
                swipeContainer.setRefreshing(false);
        }

    }

    @Override
    public void loadDirectMessage(List<DirectMessage> directMessageList) {
        mDirectMessages.clear();
        mDirectMessages.addAll(directMessageList);
        mDirectMessageAdapter.notifyDataSetChanged();
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
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        internet = isConnected;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), MessageCompose.class);
        intent.putExtra(getActivity().getString(R.string.extra_sender_id), mDirectMessages.get(position).getSenderId());
        intent.putExtra(getActivity().getString(R.string.extra_sender_name), mDirectMessages.get(position).getSender());
        startActivity(intent);
    }
}
