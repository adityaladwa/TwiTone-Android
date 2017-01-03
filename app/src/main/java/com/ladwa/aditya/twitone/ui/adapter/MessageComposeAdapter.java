package com.ladwa.aditya.twitone.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.ui.customview.ChatBubbleTextView;
import com.ladwa.aditya.twitone.data.local.models.DirectMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An Adapter for Compose Message Module
 * Created by Aditya on 31-Jul-16.
 */
public class MessageComposeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int LEFT = 0, RIGHT = 1;

    private List<DirectMessage> mDirectMessageList;
    private Context mContext;
    private DirectMessage mDirectMessage;
    private long mSenderId;

    public MessageComposeAdapter(List<DirectMessage> mDirectMessageList, Context mContext, long senderId) {
        this.mDirectMessageList = mDirectMessageList;
        this.mContext = mContext;
        this.mSenderId = senderId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LEFT:
                viewHolder = new ViewHolderLeft(inflater.inflate(R.layout.message_bubble_left, parent, false));
                break;
            case RIGHT:
                viewHolder = new ViewHolderRight(inflater.inflate(R.layout.message_bubble_right, parent, false));
                break;
            default:
                viewHolder = new ViewHolderRight(inflater.inflate(R.layout.message_bubble_right, parent, false));
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mDirectMessage = mDirectMessageList.get(position);

        switch (holder.getItemViewType()) {
            case LEFT:
                ViewHolderLeft viewHolderLeft = (ViewHolderLeft) holder;
                viewHolderLeft.textViewMessage.setText(mDirectMessage.getText());
                viewHolderLeft.textViewSender.setText(mDirectMessage.getSender());
                break;

            case RIGHT:
                ViewHolderRight viewHolderRight = (ViewHolderRight) holder;
                viewHolderRight.textViewMessage.setText(mDirectMessage.getText());
                viewHolderRight.textViewSender.setText(mDirectMessage.getSender());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mDirectMessageList == null)
            return 0;
        else return mDirectMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Long senderId = mDirectMessageList.get(position).getSenderId();
        if (senderId == mSenderId)
            return RIGHT;
        else
            return LEFT;

    }

    public class ViewHolderLeft extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_from_sender)
        TextView textViewSender;

        @BindView(R.id.textview_from_message)
        TextView textViewMessage;

        public ViewHolderLeft(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }


    public class ViewHolderRight extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_to_sender)
        TextView textViewSender;

        @BindView(R.id.textview_to_message)
        ChatBubbleTextView textViewMessage;

        public ViewHolderRight(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
