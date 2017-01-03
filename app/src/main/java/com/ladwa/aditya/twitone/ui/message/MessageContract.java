package com.ladwa.aditya.twitone.ui.message;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;

import java.util.List;

/**
 * A interface for Message Contract
 * Created by Aditya on 20-Jul-16.
 */
public interface MessageContract {

    interface View extends BaseView<Presenter> {
        void loadDirectMessage(List<DirectMessage> directMessageList);

        void stopRefreshing();

        void showError();
    }


    interface Presenter extends BasePresenter {
        void loadDirectMessage();

        void refreshRemoteDirectMessage();
    }
}
