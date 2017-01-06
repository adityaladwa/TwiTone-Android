package com.ladwa.aditya.twitone.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.util.Utility;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A Recycler View Adapter for TimeLine of the User
 * Created by Aditya on 14-Jul-16.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private List<Tweet> mTweetList;
    private Context mContext;
    private Tweet mTweet;
    private TimeLineClickListener mTimeLineClickListener;

    private ColorDrawable mColorDrawablePlaceholder = new ColorDrawable(0xFFFF6666);

    public interface TimeLineClickListener {
        void onItemClick(View view, int position);

        void onClickedReplay(View view, int position);

        void onClickedFavourite(View view, int position);

        void onClickedRetweet(View view, int position);

        void onLongClick(View view, int position);

        void onClickMedia(View view, int position);

        void onClickTweetText(View view, int position);
    }


    public TimelineAdapter(List<Tweet> mTweetList, Context mContext) {
        this.mTweetList = mTweetList;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mTweet = mTweetList.get(position);

        holder.textViewTweet.setText(mTweet.getTweet());
        holder.textViewUserName.setText(mTweet.getUserName());
        holder.textViewScreenName.setText(String.format(mContext.getString(R.string.user_name), mTweet.getScreenName()));
        holder.textViewFavCount.setText(String.valueOf(mTweet.getFavCount()));
        holder.textViewRetweetCount.setText(String.valueOf(mTweet.getRetweetCount()));
        holder.textViewDate.setText(Utility.parseDate(mTweet.getDateCreated()));

        //Load Media if available
        if (mTweet.getMediaUrl() != null) {
            holder.imageViewMedia.setVisibility(View.VISIBLE);
            holder.materialProgressBar.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mTweet.getMediaUrl())
                    .fitCenter()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.materialProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //Set progress bar visibility to null
                            holder.materialProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageViewMedia);
        } else {
            holder.imageViewMedia.setVisibility(View.GONE);
            holder.materialProgressBar.setVisibility(View.GONE);
        }

        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        String mentionScheme = "com.ladwa.aditya.twitone/" + mTweet.getId();
        Linkify.addLinks(holder.textViewTweet, mentionPattern, mentionScheme, null, filter);


        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
        String hashtagScheme = "com.ladwa.aditya.twitone/" + mTweet.getId();
        Linkify.addLinks(holder.textViewTweet, hashtagPattern, hashtagScheme, null, filter);

        Pattern urlPattern = Patterns.WEB_URL;
        Linkify.addLinks(holder.textViewTweet, urlPattern, null, null, filter);


        if (mTweet.getFav() == 1) {
            holder.imageViewFav.setColor(Color.RED);
        } else {
            holder.imageViewFav.setColor(Color.GRAY);
        }

        if (mTweet.getRetweet() == 1) {
            holder.imageViewRetweet.setColor(Color.GREEN);
        } else {
            holder.imageViewRetweet.setColor(Color.GRAY);
        }


        Glide.with(mContext)
                .load(mTweet.getProfileUrl())
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(mColorDrawablePlaceholder)
                .priority(Priority.IMMEDIATE)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (mTweet.getVerified() == 1) {
                            Glide.with(mContext)
                                    .load(R.drawable.ic_user_type_verified)
                                    .into(holder.imageViewVerified);
                        } else {
                            holder.imageViewVerified.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(holder.imageViewProfile);


    }


    @Override
    public int getItemCount() {
        if (mTweetList == null)
            return 0;
        else return mTweetList.size();

    }

    public void setTimeLineClickListner(TimeLineClickListener mTimeLineClickListener) {
        this.mTimeLineClickListener = mTimeLineClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageViewFav.setOnClickListener(this);
            imageViewRetweet.setOnClickListener(this);
            imageViewMedia.setOnClickListener(this);
            imageViewReplay.setOnClickListener(this);
            textViewTweet.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageview_fav:
                    mTimeLineClickListener.onClickedFavourite(v, getAdapterPosition());
                    break;
                case R.id.imageview_retweet:
                    mTimeLineClickListener.onClickedRetweet(v, getAdapterPosition());
                    break;
                case R.id.imageview_media:
                    mTimeLineClickListener.onClickMedia(v, getAdapterPosition());
                    break;
                case R.id.imageview_replay:
                    mTimeLineClickListener.onClickedReplay(v, getAdapterPosition());
                    break;
                case R.id.textview_tweet:
                    mTimeLineClickListener.onClickTweetText(v, getAdapterPosition());
                    break;
                default:
                    mTimeLineClickListener.onItemClick(v, getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            mTimeLineClickListener.onLongClick(v, getAdapterPosition());
            return true;
        }
    }
}
