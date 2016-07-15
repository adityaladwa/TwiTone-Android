package com.ladwa.aditya.twitone.adapter;

import android.content.Context;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Recycler View Adapter for TimeLine of the User
 * Created by Aditya on 14-Jul-16.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {


    private List<Tweet> mTweetList;
    private Context mContext;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = mTweetList.get(position);
        holder.textViewTweet.setText(tweet.getTweet());

        Glide.with(mContext).load(tweet.getProfileUrl()).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewProfile);
    }

    @Override
    public int getItemCount() {
        if (mTweetList == null)
            return 0;
        else return mTweetList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview_profile_pic)
        ImageView imageViewProfile;
        @BindView(R.id.textview_tweet)
        TextView textViewTweet;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
