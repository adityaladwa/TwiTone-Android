package com.ladwa.aditya.twitone.messagecompose;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        ConnectionReceiver.ConnectionReceiverListener, TextWatcher {

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
    private boolean first = true;
    private String senderName;


    public MessageComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_compose, container, false);

        unbinder = ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);

        internet = ConnectionReceiver.isConnected();
        senderId = getActivity().getIntent().getLongExtra(getString(R.string.extra_sender_id), 0);
        senderName = getActivity().getIntent().getStringExtra(getString(R.string.extra_sender_name));
        getActivity().setTitle(senderName);
        userId = preferences.getLong(getString(R.string.pref_userid), 0);
        String token = preferences.getString(getString(R.string.pref_access_token), "");
        String secret = preferences.getString(getString(R.string.pref_access_secret), "");

        //Create instance of presenter
        AccessToken accessToken = new AccessToken(token, secret);
        mTwitter.setOAuthAccessToken(accessToken);

        new MessageComposePresenter(this, getActivity(), senderId);
        mEditText.addTextChangedListener(this);

        setUpRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.button_message_send)
    public void onClickSendButton() {
        String message = mEditText.getText().toString();
        if (message.length() > 0) {
            if (internet) {
                mPresenter.sendDirectMessage(senderId, message);
            } else {
                Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                        .show();
            }
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mEditText.length() >= 1 && first) {
            Animation scaleUp = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
            scaleUp.setDuration(200);
            mSendImageView.startAnimation(scaleUp);
            mSendImageView.setVisibility(View.VISIBLE);
            first = false;
        } else if (mEditText.length() < 1) {
            Animation scaleDown = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
            scaleDown.setDuration(200);
            mSendImageView.startAnimation(scaleDown);
            mSendImageView.setVisibility(View.INVISIBLE);
            first = true;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
