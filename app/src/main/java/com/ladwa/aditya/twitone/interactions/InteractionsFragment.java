package com.ladwa.aditya.twitone.interactions;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.dev.debug.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class InteractionsFragment extends Fragment {

    public InteractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interactions, container, false);
    }
}
