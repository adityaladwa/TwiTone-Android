package com.ladwa.aditya.twitone.trends;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.Trend;

import java.util.List;

/**
 * A Contract class for Trends
 * Created by Aditya on 22-Jul-16.
 */
public class TrendsContract {

    interface View extends BaseView<Presenter> {
        void loadedTrends(List<Trend> trendList);

        void stopRefreshing();

        void showError();
    }

    interface Presenter extends BasePresenter {
        void loadTrends();

        void refreshRemoteTrends();
    }
}
