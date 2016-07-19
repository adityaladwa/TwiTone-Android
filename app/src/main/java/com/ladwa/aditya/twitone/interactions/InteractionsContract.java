package com.ladwa.aditya.twitone.interactions;

import com.ladwa.aditya.twitone.BasePresenter;
import com.ladwa.aditya.twitone.BaseView;

/**
 * A Contract interface that holds the blueprint of the Interactions module
 * Created by Aditya on 19-Jul-16.
 */
public class InteractionsContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
