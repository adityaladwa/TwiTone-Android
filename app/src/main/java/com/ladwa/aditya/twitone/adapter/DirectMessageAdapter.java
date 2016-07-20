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
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;
import com.ladwa.aditya.twitone.util.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An Adapter for direct message
 * Created by Aditya on 20-Jul-16.
 */
public class DirectMessageAdapter extends RecyclerView.Adapter<DirectMessageAdapter.ViewHolder> {

    private List<DirectMessage> mDirectMessageList;
    private Context mContext;
    private DirectMessage mDirectMessage;
    private DirectMessageClickListener messageClickListener;

    public DirectMessageAdapter(List<DirectMessage> mDirectMessageList, Context mContext) {
        this.mDirectMessageList = mDirectMessageList;
        this.mContext = mContext;
    }

    public interface DirectMessageClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_direct_message, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mDirectMessage = mDirectMessageList.get(position);

        holder.textViewText.setText(mDirectMessage.getText());
        holder.textViewUserName.setText(mDirectMessage.getSender());
        holder.textViewScreenName.setText(String.format(mContext.getString(R.string.user_name), mDirectMessage.getSenderScreenName()));
        holder.textViewDate.setText(Utility.parseDate(mDirectMessage.getDateCreated()));


        Glide.with(mContext)
                .load(mDirectMessage.getProfileUrl())
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.imageViewProfile);
    }

    @Override
    public int getItemCount() {
        if (mDirectMessageList == null)
            return 0;
        else return mDirectMessageList.size();
    }

    public void setMessageClickListener(DirectMessageClickListener messageClickListener) {
        this.messageClickListener = messageClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.imageview_profile_pic)
        ImageView imageViewProfile;
        @BindView(R.id.textview_text)
        TextView textViewText;
        @BindView(R.id.textview_screen_name)
        TextView textViewScreenName;
        @BindView(R.id.textview_time)
        TextView textViewDate;
        @BindView(R.id.textview_user_name)
        TextView textViewUserName;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            messageClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
