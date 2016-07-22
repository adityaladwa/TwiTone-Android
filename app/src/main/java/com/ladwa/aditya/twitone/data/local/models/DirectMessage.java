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
 * A model class for Direct message
 * Created by Aditya on 20-Jul-16.
 */
@AutoValue
@StorIOSQLiteType(table = TwitterContract.DirectMessage.TABLE_NAME)
@StorIOContentResolverType(uri = TwitterContract.DirectMessage.CONTENT_URI_STRING)
public class DirectMessage implements Parcelable {

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_ID, key = true)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_ID, key = true)
    Long id;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_DATE_CREATED)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_DATE_CREATED)
    String dateCreated;


    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_PROFILE_URL)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_PROFILE_URL)
    String profileUrl;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_RECIPIENT)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_RECIPIENT)
    String recipient;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_RECIPIENT_ID)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_RECIPIENT_ID)
    Long recipientId;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_RECIPIENT_SCREEN_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_RECIPIENT_SCREEN_NAME)
    String recipientScreenName;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_SENDER)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_SENDER)
    String sender;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_SENDER_ID)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_SENDER_ID)
    Long senderId;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_SENDER_SCREEN_NAME)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_SENDER_SCREEN_NAME)
    String senderScreenName;

    @StorIOSQLiteColumn(name = TwitterContract.DirectMessage.COLUMN_TEXT)
    @StorIOContentResolverColumn(name = TwitterContract.DirectMessage.COLUMN_TEXT)
    String text;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    public void setRecipientScreenName(String recipientScreenName) {
        this.recipientScreenName = recipientScreenName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public void setSenderScreenName(String senderScreenName) {
        this.senderScreenName = senderScreenName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    protected DirectMessage(Parcel in) {
        dateCreated = in.readString();
        profileUrl = in.readString();
        recipient = in.readString();
        recipientScreenName = in.readString();
        sender = in.readString();
        senderScreenName = in.readString();
        text = in.readString();
    }

    public static final Creator<DirectMessage> CREATOR = new Creator<DirectMessage>() {
        @Override
        public DirectMessage createFromParcel(Parcel in) {
            return new DirectMessage(in);
        }

        @Override
        public DirectMessage[] newArray(int size) {
            return new DirectMessage[size];
        }
    };

    public DirectMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateCreated);
        dest.writeString(profileUrl);
        dest.writeString(recipient);
        dest.writeString(recipientScreenName);
        dest.writeString(sender);
        dest.writeString(senderScreenName);
        dest.writeString(text);
    }
}
