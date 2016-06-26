package com.ladwa.aditya.twitone.login;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;
import twitter4j.AsyncTwitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment implements LoginContract.View {

    private static final String TAG = LoginActivityFragment.class.getSimpleName();

    @BindView(R.id.twitter_login_button)
    Button loginButton;

    @BindView(R.id.login_webview)
    WebView mWebView;
    private LoginContract.Presenter mPresenter;

    @Inject
    SharedPreferences preferences;

    @Inject
    AsyncTwitter twitter;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        ((TwitoneApp) getActivity().getApplicationContext()).getTwitterComponent().inject(this);
        new LoginPresenter(this, twitter);


        return view;
    }

    @OnClick(R.id.twitter_login_button)
    void twitterLogin() {
        mPresenter.login();
        getActivity().setTheme(R.style.AppThemeDark);
        loginButton.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setSaveFormData(false);
        mWebView.getSettings().setJavaScriptEnabled(true);


        // Get the access token


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url != null && url.startsWith("oauth://twitoneforandroid")) {
                    String verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
                    Log.d(TAG, "Verifier is :" + verifier);
                    mPresenter.getAccessToken(verifier);
                    mWebView.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "URI error or URI is null");
                }

                return true;

            }
        });

    }

    @Override
    public void onError() {
        Toast.makeText(getActivity(), "There is some error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(), "Welcome to " + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
