package com.ladwa.aditya.twitone.interactions;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.Interaction;

import java.util.List;

/**
 * A Contract interface that holds the blueprint of the Interactions module
 * Created by Aditya on 19-Jul-16.
 */
public class InteractionsContract {

    interface View extends BaseView<Presenter> {
        void loadInteractions(List<Interaction> interactionList);
    }

    interface Presenter extends BasePresenter {

        void getUserInteractions();
    }
}
