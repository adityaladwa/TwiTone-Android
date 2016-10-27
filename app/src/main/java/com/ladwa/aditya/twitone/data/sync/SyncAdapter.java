package com.ladwa.aditya.twitone.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.TwitoneApp;
import com.ladwa.aditya.twitone.data.local.TwitterLocalDataStore;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.data.remote.TwitterRemoteDataSource;
import com.ladwa.aditya.twitone.util.NotificationUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A {@link SyncAdapter} class to Sync data
 * Created by Aditya on 23-Jul-16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String ACCOUNT_TYPE = "com.ladwa.aditya.twitone";
    public static final String ACCOUNT = "Twitone";
    public static final int SYNC_INTERVAL = 60 * 180;

    @Inject
    TwitterLocalDataStore mTwitterLocalDataStore;

    @Inject
    TwitterRemoteDataSource mTwitterRemoteDataSource;

    private Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        TwitoneApp.getTwitterComponent().inject(this);
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        long lastTweetId = mTwitterLocalDataStore.getLastTweetId();

        mTwitterRemoteDataSource.getTimeLine(lastTweetId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Tweet>>() {
                    @Override
                    public void onCompleted() {
//                        Timber.d("Sync is performed");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Timber.d(e.toString());
                    }

                    @Override
                    public void onNext(List<Tweet> tweetList) {
                        if (tweetList.size() > 0) {
                            NotificationUtil.showNotification(mContext, tweetList.size(), tweetList.get(0).getTweet(), tweetList);
                        }
                    }
                });


    }

    public static Account getSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);


        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "pass", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static void configurePeriodicSync(Context context, int syncInterval) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);


        ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);

    }

    private static void onAccountCreated(Account account, Context context) {
        ContentResolver.setIsSyncable(account, context.getString(R.string.content_authority), 1);

        ContentResolver.setSyncAutomatically(account, context.getString(R.string.content_authority), true);

        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL);

        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void removeUserAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        accountManager.removeAccount(new Account(ACCOUNT, ACCOUNT_TYPE), null, null);

    }
}
