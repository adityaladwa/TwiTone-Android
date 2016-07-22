package com.ladwa.aditya.twitone.trends;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladwa.aditya.twitone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalTrendsFragment extends Fragment {


    public LocalTrendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_trends, container, false);
    }

}
