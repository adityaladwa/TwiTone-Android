package com.ladwa.aditya.twitone.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.ui.base.BaseFragment;
import com.ladwa.aditya.twitone.ui.mainscreen.MainScreen;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends BaseFragment implements LoginContract.View, ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = LoginActivityFragment.class.getSimpleName();

    @BindView(R.id.twitter_login_button) Button loginButton;
    @BindView(R.id.login_webview) WebView mWebView;
    @BindView(R.id.progressBar) MaterialProgressBar mProgressBar;

    @Inject SharedPreferences preferences;
    @Inject LoginPresenter loginPresenter;

    private Unbinder unbinder;
    private boolean internet;


    public LoginActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);
        return view;
    }


    @OnClick(R.id.twitter_login_button)
    void twitterLogin() {
        if (internet) {
            loginPresenter.login();
            loginButton.setVisibility(View.GONE);
            mWebView.setBackgroundColor(getActivity().getResources().getColor(R.color.grey));
            mWebView.setVisibility(View.VISIBLE);
            mWebView.getSettings().setSaveFormData(false);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new MyWebViewClient());
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            Snackbar.make(mWebView, "Please check your internet", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(getActivity(), "There is some error " + errorMessage, Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String message) {
        startActivity(new Intent(getActivity(), MainScreen.class));
        getActivity().finish();
    }

    @Override
    public void startOauthIntent(RequestToken requestToken) {
        mWebView.loadUrl(requestToken.getAuthenticationURL());
    }

    @Override
    public void saveAccessTokenAnd(AccessToken accessToken) {
        if (accessToken != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.pref_login), true);
            editor.putString(getString(R.string.pref_access_token), accessToken.getToken());
            editor.putString(getString(R.string.pref_access_secret), accessToken.getTokenSecret());
            editor.putLong(getString(R.string.pref_userid), accessToken.getUserId());
            editor.putString(getString(R.string.pref_screen_name), accessToken.getScreenName());
            editor.apply();
            Log.d(TAG, "Saved Credential to Shared Pref");
        } else {
            Log.d(TAG, "Access token is null");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginPresenter.attachView(this);
        internet = ConnectionReceiver.isConnected();
        ConnectionReceiver.setConnectionReceiverListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        loginPresenter.detachView();
        ConnectionReceiver.destoryInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TwitoneApp.getRefWatcher();
        refWatcher.watch(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        internet = isConnected;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && url.startsWith("oauth://twitoneforandroid")) {
                String verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
                Log.d(TAG, "Verifier is :" + verifier);
                loginPresenter.getAccessToken(verifier);
                mWebView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
