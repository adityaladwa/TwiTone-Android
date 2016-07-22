package com.ladwa.aditya.twitone.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ladwa.aditya.twitone.TwitoneApp;

/**
 * A BroadCast Receiver to listen for Connectivity Changes
 * {@link BroadcastReceiver}
 * Created by Aditya on 28-Jun-16.
 */
public class ConnectionReceiver extends BroadcastReceiver {

    public static ConnectionReceiverListener connectionReceiverListener;

    public ConnectionReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectionReceiverListener != null) {
            connectionReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static void destoryInstance() {
        connectionReceiverListener = null;
    }

    public static void setConnectionReceiverListener(ConnectionReceiverListener listener) {
        connectionReceiverListener = listener;
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) TwitoneApp.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectionReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
