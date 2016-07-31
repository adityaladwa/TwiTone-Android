package com.ladwa.aditya.twitone.messagecompose;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;

import java.util.List;

/**
 * A Contract Class for Message Compose
 * Created by Aditya on 31-Jul-16.
 */
public class MessageComposeContract {

    interface View extends BaseView<Presenter> {
        void loadUserDirectMessage(List<DirectMessage> directMessageList);

        void stopRefreshing();

        void showError();

        void setUpRecyclerView();
    }


    interface Presenter extends BasePresenter {
        void getUserDirectMessage();

    }
}
