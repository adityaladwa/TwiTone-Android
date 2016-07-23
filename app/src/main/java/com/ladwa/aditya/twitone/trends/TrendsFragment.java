package com.ladwa.aditya.twitone.trends;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.adapter.TrendAdapter;
import com.ladwa.aditya.twitone.data.TwitterRepository;
import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.ladwa.aditya.twitone.data.local.models.Trend;
import com.ladwa.aditya.twitone.util.ConnectionReceiver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrendsFragment extends Fragment implements TrendsContract.View,
        ConnectionReceiver.ConnectionReceiverListener,
        SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    @Inject
    TwitterRepository repository;

    @BindView(R.id.recyclerview_trends)
    RecyclerView recyclerView;

    @BindView(R.id.swipeContainer_trends)
    SwipeRefreshLayout swipeContainer;

    private boolean internet;
    private Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    private TrendsContract.Presenter mPresenter;
    private List<Trend> mTrends;
    private TrendAdapter mTrendAdapter;

    public TrendsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        unbinder = ButterKnife.bind(this, view);
        TwitoneApp.getTwitterComponent().inject(this);

        new TrendsPresenter(this, repository, "Global", getActivity());

        //Recycler View
        linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        mTrends = new ArrayList<>();
        mTrendAdapter = new TrendAdapter(mTrends, getActivity());
        recyclerView.setAdapter(mTrendAdapter);
        swipeContainer.setOnRefreshListener(this);

        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
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
    public void loadedTrends(List<Trend> trendList) {
//        mTrends.clear();
//        mTrends.addAll(trendList);
//        mTrendAdapter.notifyDataSetChanged();
    }

    @Override
    public void stopRefreshing() {
        if (swipeContainer != null)
            swipeContainer.setRefreshing(false);
    }

    @Override
    public void showRefreshing() {
        if (swipeContainer != null)
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);
                }
            });
    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_occured, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void setPresenter(TrendsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        internet = isConnected;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), TwitterContract.Trends.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            mTrends.clear();
            List<Trend> trendList = new ArrayList<>();
            try {
                while (data.moveToNext()) {
                    Trend t = new Trend();
                    t.setTrend(data.getString(data.getColumnIndex(TwitterContract.Trends.COLUMN_TREND)));
                    trendList.add(t);
                }
            } finally {
                data.close();
                mTrends.addAll(trendList);
                mTrendAdapter.notifyDataSetChanged();
            }

        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onRefresh() {
        if (internet) {
            mPresenter.refreshRemoteTrends();
        } else {
            Snackbar.make(recyclerView, R.string.check_internet, Snackbar.LENGTH_LONG)
                    .show();
            if (swipeContainer != null)
                swipeContainer.setRefreshing(false);
        }
    }
}
