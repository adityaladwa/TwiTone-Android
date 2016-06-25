package com.ladwa.aditya.twitone.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment implements LoginContract.View {

    @BindView(R.id.twitter_login_button)
    Button loginButton;
    private LoginContract.Presenter mPresenter;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        new LoginPresenter(this);
        return view;
    }

    @OnClick(R.id.twitter_login_button)
    void twitterLogin() {
        mPresenter.login();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(), "Login Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
