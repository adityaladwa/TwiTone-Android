package com.ladwa.aditya.twitone.tweet;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ladwa.aditya.twitone.R;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.view.IconicsImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class Tweet extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_REQUEST_CODE = 10;
    @BindView(R.id.fab_tweet_activity)
    FloatingActionButton mFab;

    @BindView(R.id.tweet_activity_container)
    CoordinatorLayout mRlContainer;

    @BindView(R.id.tweet_appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.edittext_tweet)
    EditText mEditTextTweet;

    @BindView(R.id.relativelayout_tweet_footer)
    RelativeLayout mRelativeLayoutTweetFooter;

    @BindView(R.id.imageview_location)
    IconicsImageView mImageViewLocation;

    @BindView(R.id.imageview_place_icon)
    IconicsImageView mImageViewPlace;

    @BindView(R.id.linear_layout_place)
    LinearLayout mLinearLayoutPlace;

    @BindView(R.id.textview_location)
    TextView mTextViewLocation;

    @BindView(R.id.button_tweet)
    Button mButtonTweet;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    boolean click = true;
    boolean location = false;
    private double latitude;
    private double longitude;
    private Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpEnterAnimation();
        setupExitAnimation();


    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.imageview_location)
    public void onClickLocation() {
        if (click) {
            mImageViewLocation.setColor(getResources().getColor(R.color.md_blue_700));
            click = false;
            location = true;
            getUserLocation();

        } else {
            mImageViewLocation.setColor(getResources().getColor(R.color.md_black_1000));
            click = true;
            location = false;
            unSetLocation();
        }


    }

    private void unSetLocation() {
        Animation fadeIn = AnimationUtils.loadAnimation(Tweet.this, android.R.anim.fade_out);
        fadeIn.setDuration(1000);

        Animation scaleUp = AnimationUtils.loadAnimation(Tweet.this, R.anim.scale_down);
        scaleUp.setDuration(700);
        if (mImageViewPlace.getVisibility() == View.VISIBLE) {
            mImageViewPlace.startAnimation(scaleUp);
            mTextViewLocation.startAnimation(scaleUp);
            mLinearLayoutPlace.startAnimation(fadeIn);
            mImageViewPlace.setVisibility(View.INVISIBLE);
            mTextViewLocation.setVisibility(View.INVISIBLE);
            mLinearLayoutPlace.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private void setUpEnterAnimation() {

        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        transition.setDuration(300);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Timber.d("startAnim");
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow(mRlContainer);
                Timber.d("endAnim");
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

    }

    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(300);
    }

    @Override
    public void onBackPressed() {
        mFab.setVisibility(View.VISIBLE);
        animateRevealHide(this, mRlContainer, R.color.md_white_1000, mFab.getWidth() / 2,
                new OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        backPressed();
                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    private void backPressed() {
        super.onBackPressed();
    }

    private void animateRevealShow(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        animateRevealShow(this, viewRoot, mFab.getWidth() / 2, R.color.md_white_1000,
                cx, cy, new OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {
                        initViews();
                    }
                });
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);

            Timber.d("No permission");
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            List<Address> fromLocation = null;
            Timber.d("Location " + latitude + "-" + longitude);
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                fromLocation = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String locality = "Unknown Location";

            if (fromLocation != null) {
                locality = fromLocation.get(0).getLocality();
                mTextViewLocation.setText(locality);

                Animation fadeIn = AnimationUtils.loadAnimation(Tweet.this, android.R.anim.fade_in);
                fadeIn.setDuration(1000);

                Animation scaleUp = AnimationUtils.loadAnimation(Tweet.this, R.anim.scale_up);
                scaleUp.setDuration(700);

                mImageViewPlace.startAnimation(scaleUp);
                mImageViewPlace.setVisibility(View.VISIBLE);

                mTextViewLocation.startAnimation(scaleUp);
                mTextViewLocation.setVisibility(View.VISIBLE);

                mLinearLayoutPlace.startAnimation(fadeIn);
                mLinearLayoutPlace.setVisibility(View.VISIBLE);

            } else {
                mImageViewLocation.setColor(getResources().getColor(R.color.md_black_1000));
                click = true;
                location = false;
                Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();
            }


        } else {
            mImageViewLocation.setColor(getResources().getColor(R.color.md_black_1000));
            click = true;
            location = false;
            Toast.makeText(this, R.string.cant_determine_location, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (location)
            getUserLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, R.string.location_suspended, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.location_failed, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation();
                } else {
                    Toast.makeText(this, "Location needed to show Local Trends", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public interface OnRevealAnimationListener {
        void onRevealHide();

        void onRevealShow();
    }


    public static void animateRevealShow(final Context ctx, final View view, final int startRadius,
                                         @ColorRes final int color, int x, int y, final OnRevealAnimationListener listener) {
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, finalRadius);
        anim.setDuration(500);
        anim.setStartDelay(80);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setBackgroundColor(ContextCompat.getColor(ctx, color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
                if (listener != null) {
                    listener.onRevealShow();
                }
            }
        });
        anim.start();
    }

    public static void animateRevealHide(final Context ctx, final View view, @ColorRes final int color,
                                         final int finalRadius, final OnRevealAnimationListener listener) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        int initialRadius = view.getWidth();

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ctx.getResources().getColor(color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onRevealHide();
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(300);
        anim.start();
    }

    private void initViews() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = AnimationUtils.loadAnimation(Tweet.this, android.R.anim.fade_in);
                fadeIn.setDuration(300);
                mAppBarLayout.startAnimation(fadeIn);
                mAppBarLayout.setVisibility(View.VISIBLE);

                mRelativeLayoutTweetFooter.startAnimation(fadeIn);
                mRelativeLayoutTweetFooter.setVisibility(View.VISIBLE);

                mEditTextTweet.startAnimation(fadeIn);
                mEditTextTweet.setVisibility(View.VISIBLE);

                Animation fadeOut = AnimationUtils.loadAnimation(Tweet.this, android.R.anim.fade_out);
                fadeOut.setDuration(100);
                mFab.startAnimation(fadeOut);
                mFab.setVisibility(View.GONE);


                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        //Show keyboard
                        mEditTextTweet.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEditTextTweet, InputMethodManager.SHOW_IMPLICIT);

                        //Scale up location Icon
                        Animation scaleUp = AnimationUtils.loadAnimation(Tweet.this, R.anim.scale_up);
                        scaleUp.setDuration(700);
                        mImageViewLocation.startAnimation(scaleUp);
                        mImageViewLocation.setVisibility(View.VISIBLE);

                        mButtonTweet.startAnimation(scaleUp);
                        mButtonTweet.setVisibility(View.VISIBLE);




                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

}
