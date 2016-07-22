package com.ladwa.aditya.twitone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.models.Tweet;
import com.ladwa.aditya.twitone.util.Utility;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A Recycler View Adapter for TimeLine of the User
 * Created by Aditya on 14-Jul-16.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {


    private List<Tweet> mTweetList;
    private Context mContext;
    private IconicsDrawable retweetIcon, favIcon;
    private Tweet mTweet;
    private TimeLineClickListener mTimeLineClickListener;

    public interface TimeLineClickListener {
        void onItemClick(View view, int position);

        void onClickedFavourite(View view, int position);

        void onClickedRetweet(View view, int position);

        void onLongClick(View view, int position);
    }


    public TimelineAdapter(List<Tweet> mTweetList, Context mContext) {
        this.mTweetList = mTweetList;
        this.mContext = mContext;
        this.retweetIcon = new IconicsDrawable(mContext).icon(FontAwesome.Icon.faw_retweet);
        this.favIcon = new IconicsDrawable(mContext).icon(FontAwesome.Icon.faw_heart);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mTweet = mTweetList.get(position);
        holder.textViewTweet.setText(mTweet.getTweet());
        holder.textViewUserName.setText(mTweet.getUserName());
        holder.textViewScreenName.setText(String.format(mContext.getString(R.string.user_name), mTweet.getScreenName()));
        holder.textViewFavCount.setText(String.valueOf(mTweet.getFavCount()));
        holder.textViewRetweetCount.setText(String.valueOf(mTweet.getRetweetCount()));
        holder.textViewDate.setText(Utility.parseDate(mTweet.getDateCreated()));

        if (mTweetList.get(position).getVerified() == 1) {
            holder.imageViewVerified.setImageDrawable(mContext.getDrawable(R.drawable.ic_user_type_verified));
        } else {
            holder.imageViewVerified.setVisibility(View.GONE);
        }

        if (mTweetList.get(position).getFav() == 1) {
            holder.imageViewFav.setImageDrawable(favIcon.color(Color.RED));
        } else {
            holder.imageViewFav.setImageDrawable(favIcon.color(Color.GRAY));
        }

        if (mTweetList.get(position).getRetweet() == 1) {
            holder.imageViewRetweet.setImageDrawable(retweetIcon.color(Color.GREEN));
        } else {
            holder.imageViewRetweet.setImageDrawable(retweetIcon.color(Color.GRAY));
        }


        Glide.with(mContext)
                .load(mTweet.getProfileUrl())
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
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
        Timber.d("Click listener is set");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.imageview_profile_pic)
        ImageView imageViewProfile;
        @BindView(R.id.imageview_verified)
        ImageView imageViewVerified;
        @BindView(R.id.textview_tweet)
        TextView textViewTweet;
        @BindView(R.id.textview_screen_name)
        TextView textViewScreenName;
        @BindView(R.id.textview_time)
        TextView textViewDate;
        @BindView(R.id.textview_user_name)
        TextView textViewUserName;
        @BindView(R.id.imageview_retweet)
        ImageView imageViewRetweet;
        @BindView(R.id.imageview_fav)
        ImageView imageViewFav;
        @BindView(R.id.textview_retweet_count)
        TextView textViewRetweetCount;
        @BindView(R.id.textview_fav_count)
        TextView textViewFavCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageViewFav.setOnClickListener(this);
            imageViewRetweet.setOnClickListener(this);
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
