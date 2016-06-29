package com.ladwa.aditya.twitone.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.mainscreen.MainScreen;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import timber.log.Timber;
import twitter4j.AsyncTwitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment implements LoginContract.View, ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = LoginActivityFragment.class.getSimpleName();

    @BindView(R.id.twitter_login_button)
    Button loginButton;

    @BindView(R.id.login_webview)
    WebView mWebView;

    //    @BindView(R.id.progress)
//    SmoothProgressBar mSmoothProgressBar;
    @BindView(R.id.progressBar)
    MaterialProgressBar mProgressBar;
    @Inject
    SharedPreferences preferences;
    @Inject
    AsyncTwitter twitter;

    private LoginContract.Presenter mPresenter;

    private boolean internet;


    public LoginActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);
        internet = ConnectionReceiver.isConnected();
        new LoginPresenter(this, twitter);


        return view;
    }


    @OnClick(R.id.twitter_login_button)
    void twitterLogin() {
        if (internet) {
            mPresenter.login();
            loginButton.setVisibility(View.GONE);
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
//        mSmoothProgressBar.progressiveStop();
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
            Timber.d("Saved credential");
        } else {
            Log.d(TAG, "Access token is null");
        }
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
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TwitoneApp.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Timber.d("Internet changed");
        internet = isConnected;
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url != null && url.startsWith("oauth://twitoneforandroid")) {
                String verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
                Log.d(TAG, "Verifier is :" + verifier);
                mPresenter.getAccessToken(verifier);
                mWebView.setVisibility(View.GONE);
//                mSmoothProgressBar.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            } else {
                Timber.d("URI error or URI is null");
            }

            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            mSmoothProgressBar.setVisibility(View.VISIBLE);
//            mSmoothProgressBar.progressiveStart();
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            mSmoothProgressBar.progressiveStop();
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
