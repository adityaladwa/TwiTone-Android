package com.ladwa.aditya.twitone;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;

import com.ladwa.aditya.twitone.mainscreen.MainScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * A test to check functionality of Logout Button
 * Created by Aditya on 30-Jun-16.
 */
public class LogoutTest {

    @Rule
    public ActivityTestRule<MainScreen> mainScreenActivityTestRule = new ActivityTestRule<>(MainScreen.class);

    @Before()
    public void initSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TwitoneApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(mainScreenActivityTestRule.getActivity().getString(R.string.pref_login), true);
        editor.apply();

    }

//    @Test
//    public void clickLogoutButton_LogoutUser() throws Exception {
//        onView(withId(R.id.twitter_logout_button))
//                .perform(click());
//
//    }

    @After
    public void removeSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TwitoneApp.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(mainScreenActivityTestRule.getActivity().getString(R.string.pref_login), false);
        editor.apply();
    }
}
