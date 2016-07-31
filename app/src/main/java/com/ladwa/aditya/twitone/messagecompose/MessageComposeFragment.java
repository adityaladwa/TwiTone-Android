package com.ladwa.aditya.twitone.messagecompose;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageComposeFragment extends Fragment {

    public MessageComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_compose, container, false);
    }
}
