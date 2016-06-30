package com.ladwa.aditya.twitone;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ladwa.aditya.twitone.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * A class to test the Login Button
 * Created by Aditya on 30-Jun-16.
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);


    @Test
    public void clickLogin_LoginsUser() throws Exception {

        onView(withId(R.id.twitter_login_button)).perform(click());

    }

}
