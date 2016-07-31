package com.ladwa.aditya.twitone.messagecompose;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.adapter.MessageComposeAdapter;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageComposeFragment extends Fragment implements MessageComposeContract.View,
        ConnectionReceiver.ConnectionReceiverListener {

    @Inject
    SharedPreferences preferences;
    @Inject
    Twitter mTwitter;
    @Inject
    TwitterRepository repository;


    @BindView(R.id.recyclerview_message)
    RecyclerView recyclerView;

    @BindView(R.id.edittext_message)
    EditText mEditText;

    @BindView(R.id.button_message_send)
    IconicsImageView mSendImageView;


    private MessageComposeContract.Presenter mPresenter;
    private Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    private List<DirectMessage> mDirectMessages;
    private boolean internet;
    private MessageComposeAdapter mMessageComposeAdapter;
    private long senderId;
    private long userId;


    public MessageComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_compose, container, false);

        unbinder = ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);

        senderId = getActivity().getIntent().getLongExtra(getString(R.string.extra_sender_id), 0);
        userId = preferences.getLong(getString(R.string.pref_userid), 0);
        String token = preferences.getString(getString(R.string.pref_access_token), "");
        String secret = preferences.getString(getString(R.string.pref_access_secret), "");

        //Create instance of presenter
        AccessToken accessToken = new AccessToken(token, secret);
        mTwitter.setOAuthAccessToken(accessToken);

        new MessageComposePresenter(this, repository, getActivity(), senderId);


        setUpRecyclerView();

        return view;
    }

    @OnClick(R.id.button_message_send)
    public void onClickSendButton() {
        String message = mEditText.getText().toString();
        if (message.length() > 0) {
            mPresenter.sendDirectMessage(senderId, message);
        } else {
            Snackbar.make(recyclerView, "Type something...", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void setUpRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDirectMessages = new ArrayList<>();
        mMessageComposeAdapter = new MessageComposeAdapter(mDirectMessages, getActivity(), userId);
        recyclerView.setAdapter(mMessageComposeAdapter);
    }

    @Override
    public void clearEditText() {
        mEditText.setText("");
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
    public void loadUserDirectMessage(List<DirectMessage> directMessageList) {
        mDirectMessages.clear();
        mDirectMessages.addAll(directMessageList);
        mMessageComposeAdapter.notifyDataSetChanged();
    }

    @Override
    public void stopRefreshing() {

    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_occured, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void setPresenter(MessageComposeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        internet = isConnected;
    }
}
