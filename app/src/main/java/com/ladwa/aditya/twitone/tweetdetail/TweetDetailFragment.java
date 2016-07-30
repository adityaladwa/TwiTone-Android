package com.ladwa.aditya.twitone.tweetdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.util.Utility;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A placeholder fragment containing a simple view.
 */
public class TweetDetailFragment extends Fragment implements TweetDetailPresenter.View {

    @BindView(R.id.imageview_profile_pic)
    ImageView imageViewProfile;
    @BindView(R.id.imageview_verified)
    ImageView imageViewVerified;
    @BindView(R.id.textview_tweet)
    TextView textViewTweet;
    @BindView(R.id.imageview_media)
    ImageView imageViewMedia;
    @BindView(R.id.progressBar_timeline)
    MaterialProgressBar materialProgressBar;
    @BindView(R.id.textview_screen_name)
    TextView textViewScreenName;
    @BindView(R.id.textview_time)
    TextView textViewDate;
    @BindView(R.id.textview_user_name)
    TextView textViewUserName;
    @BindView(R.id.imageview_retweet)
    IconicsImageView imageViewRetweet;
    @BindView(R.id.imageview_fav)
    IconicsImageView imageViewFav;
    @BindView(R.id.textview_retweet_count)
    TextView textViewRetweetCount;
    @BindView(R.id.textview_fav_count)
    TextView textViewFavCount;
    @BindView(R.id.imageview_replay)
    ImageView imageViewReplay;

    public TweetDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_detail, container, false);
        ButterKnife.bind(this, view);
        Tweet tweet = getActivity().getIntent().getParcelableExtra(getActivity().getString(R.string.extra_tweet_parcle));
        Glide.with(getActivity())
                .load(tweet.getProfileUrl())
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageViewProfile);


        textViewTweet.setText(tweet.getTweet());
        textViewUserName.setText(tweet.getUserName());
        textViewScreenName.setText(String.format(getActivity().getString(R.string.user_name), tweet.getScreenName()));
        textViewFavCount.setText(String.valueOf(tweet.getFavCount()));
        textViewRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        textViewDate.setText(Utility.parseDate(tweet.getDateCreated()));

        //Load Media if available
        if (tweet.getMediaUrl() != null) {
            imageViewMedia.setVisibility(View.VISIBLE);
            materialProgressBar.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(tweet.getMediaUrl())
                    .fitCenter()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //Set progress bar visibility to null
                            materialProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageViewMedia);
        } else {
            imageViewMedia.setVisibility(View.GONE);
            materialProgressBar.setVisibility(View.GONE);
        }

        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        String mentionScheme = "com.ladwa.aditya.twitone/" + tweet.getId();
        Linkify.addLinks(textViewTweet, mentionPattern, mentionScheme, null, filter);


        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
        String hashtagScheme = "com.ladwa.aditya.twitone/" + tweet.getId();
        Linkify.addLinks(textViewTweet, hashtagPattern, hashtagScheme, null, filter);

        Pattern urlPattern = Patterns.WEB_URL;
        Linkify.addLinks(textViewTweet, urlPattern, null, null, filter);


        if (tweet.getVerified() == 1) {
            Glide.with(getActivity())
                    .load(R.drawable.ic_user_type_verified)
                    .into(imageViewVerified);
        } else {
            imageViewVerified.setVisibility(View.GONE);
        }

        if (tweet.getFav() == 1) {
            imageViewFav.setColor(Color.RED);
        } else {
            imageViewFav.setColor(Color.GRAY);
        }

        if (tweet.getRetweet() == 1) {
            imageViewRetweet.setColor(Color.GREEN);
        } else {
            imageViewRetweet.setColor(Color.GRAY);
        }

        return view;
    }

    @Override
    public void setPresenter(TweetDetailPresenter.Presenter presenter) {

    }
}
