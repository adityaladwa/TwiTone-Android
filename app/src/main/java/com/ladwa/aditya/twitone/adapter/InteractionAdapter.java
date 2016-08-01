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
import com.ladwa.aditya.twitone.data.local.models.Interaction;
import com.ladwa.aditya.twitone.util.Utility;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aditya on 20-Jul-16.
 */
public class InteractionAdapter extends RecyclerView.Adapter<InteractionAdapter.ViewHolder> {

    private List<Interaction> mInteractionsList;
    private Context mContext;
    private IconicsDrawable retweetIcon, favIcon;
    private Interaction mInteraction;
    private InteractionClickListener mInteractionClickListener;

    public InteractionAdapter(List<Interaction> mInteractions, Context mContext) {
        this.mInteractionsList = mInteractions;
        this.mContext = mContext;
        this.retweetIcon = new IconicsDrawable(mContext).icon(FontAwesome.Icon.faw_retweet);
        this.favIcon = new IconicsDrawable(mContext).icon(FontAwesome.Icon.faw_heart);
    }


    public interface InteractionClickListener {
        void onItemClick(View view, int position);

        void onClickedFavourite(View view, int position);

        void onClickedRetweet(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interactions, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mInteraction = mInteractionsList.get(position);

        holder.textViewTweet.setText(mInteraction.getTweet());
        holder.textViewUserName.setText(mInteraction.getUserName());
        holder.textViewScreenName.setText(String.format(mContext.getString(R.string.user_name), mInteraction.getScreenName()));
        holder.textViewFavCount.setText(String.valueOf(mInteraction.getFavCount()));
        holder.textViewRetweetCount.setText(String.valueOf(mInteraction.getRetweetCount()));
        holder.textViewDate.setText(Utility.parseDate(mInteraction.getDateCreated()));

        if (mInteractionsList.get(position).getVerified() == 1) {
            Glide.with(mContext)
                    .load(R.drawable.ic_user_type_verified)
                    .into(holder.imageViewVerified);
        } else {
            holder.imageViewVerified.setVisibility(View.GONE);
        }

        if (mInteractionsList.get(position).getFav() == 1) {
            Glide.with(mContext)
                    .load("")
                    .placeholder(favIcon.color(Color.RED))
                    .into(holder.imageViewFav);
        } else {
            Glide.with(mContext)
                    .load("")
                    .placeholder(favIcon.color(Color.GRAY))
                    .into(holder.imageViewFav);
        }

        if (mInteractionsList.get(position).getRetweet() == 1) {
            Glide.with(mContext)
                    .load("")
                    .placeholder(retweetIcon.color(Color.GREEN))
                    .into(holder.imageViewRetweet);
        } else {
            Glide.with(mContext)
                    .load("")
                    .placeholder(retweetIcon.color(Color.GRAY))
                    .into(holder.imageViewRetweet);
        }


        Glide.with(mContext)
                .load(mInteraction.getProfileUrl())
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.imageViewProfile);
    }

    @Override
    public int getItemCount() {
        if (mInteractionsList == null)
            return 0;
        else return mInteractionsList.size();
    }

    public void setmInteractionClickListener(InteractionClickListener mInteractionClickListener) {
        this.mInteractionClickListener = mInteractionClickListener;
//        Timber.d("Click listener is set");

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
                    mInteractionClickListener.onClickedFavourite(v, getAdapterPosition());
                    break;
                case R.id.imageview_retweet:
                    mInteractionClickListener.onClickedRetweet(v, getAdapterPosition());
                    break;
                default:
                    mInteractionClickListener.onItemClick(v, getAdapterPosition());

            }
        }

        @Override
        public boolean onLongClick(View v) {
            mInteractionClickListener.onLongClick(v, getAdapterPosition());
            return true;
        }
    }
}
