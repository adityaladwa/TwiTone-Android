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

        void setScrollPos();

        void stopRefreshing();

        void showError();

        void saveScrollPosition();

        void createdFavouriteCallback(Interaction interaction);

        void destroyFavouriteCallback(Interaction interaction);

        void createRetweetCallback(Interaction interaction);
    }

    interface Presenter extends BasePresenter {

        void loadInteractions();

        void refreshRemoteInteraction();
    }
}
