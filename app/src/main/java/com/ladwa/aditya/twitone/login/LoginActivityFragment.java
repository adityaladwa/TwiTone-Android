package com.ladwa.aditya.twitone.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment implements LoginContract.View {

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }
}
