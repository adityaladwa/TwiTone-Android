package com.ladwa.aditya.twitone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ladwa.aditya.twitone.R;
import com.ladwa.aditya.twitone.data.local.models.Trend;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An Adapter for Trends
 * Created by Aditya on 22-Jul-16.
 */
public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.ViewHolder> {
    private List<Trend> mTrendList;
    private Context mContext;

    public TrendAdapter(List<Trend> mTrendList, Context mContext) {
        this.mTrendList = mTrendList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewTrend.setText(mTrendList.get(position).getTrend());
    }

    @Override
    public int getItemCount() {
        if (mTrendList != null)
            return mTrendList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_trend)
        TextView textViewTrend;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
