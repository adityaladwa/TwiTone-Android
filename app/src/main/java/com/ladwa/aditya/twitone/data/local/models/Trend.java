package com.ladwa.aditya.twitone.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.ladwa.aditya.twitone.data.local.TwitterContract;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverType;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * A model for Trend
 * Created by Aditya on 22-Jul-16.
 */
@AutoValue
@StorIOSQLiteType(table = TwitterContract.Trends.TABLE_NAME)
@StorIOContentResolverType(uri = TwitterContract.Trends.CONTENT_URI_STRING)
public class Trend implements Parcelable {


    @StorIOSQLiteColumn(name = TwitterContract.Trends.COLUMN_TREND, key = true)
    @StorIOContentResolverColumn(name = TwitterContract.Trends.COLUMN_TREND, key = true)
    String trend;

    @StorIOSQLiteColumn(name = TwitterContract.Trends.COLUMN_DATE_CREATED)
    @StorIOContentResolverColumn(name = TwitterContract.Trends.COLUMN_DATE_CREATED)
    String dateCreated;

    @StorIOSQLiteColumn(name = TwitterContract.Trends.COLUMN_LOCAL)
    @StorIOContentResolverColumn(name = TwitterContract.Trends.COLUMN_LOCAL)
    int local;

    public Trend() {
    }


    protected Trend(Parcel in) {
        trend = in.readString();
        dateCreated = in.readString();
        local = in.readInt();
    }

    public static final Creator<Trend> CREATOR = new Creator<Trend>() {
        @Override
        public Trend createFromParcel(Parcel in) {
            return new Trend(in);
        }

        @Override
        public Trend[] newArray(int size) {
            return new Trend[size];
        }
    };


    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trend);
        dest.writeString(dateCreated);
        dest.writeInt(local);
    }
}
