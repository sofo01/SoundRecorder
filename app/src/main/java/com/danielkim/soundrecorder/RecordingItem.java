package com.danielkim.soundrecorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Daniel on 12/30/2014.
 */
public class RecordingItem implements Parcelable {
    private String mName; // file name
    private String mFilePath; //file path
    private int mId; //id in database
    private int mLength; // length of recording in seconds
    private long mTime; // date/time of the recording
    private int mFavourite; //SQLLite does not support boolean format
                            // so it will use values of 1 or 0

    public RecordingItem() {}

    protected RecordingItem(Parcel in) {
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mLength = in.readInt();
        mTime = in.readLong();
        mFavourite = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mFilePath);
        dest.writeInt(mId);
        dest.writeInt(mLength);
        dest.writeLong(mTime);
        dest.writeInt(mFavourite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecordingItem> CREATOR = new Creator<RecordingItem>() {
        @Override
        public RecordingItem createFromParcel(Parcel in) {
            return new RecordingItem(in);
        }

        @Override
        public RecordingItem[] newArray(int size) {
            return new RecordingItem[size];
        }
    };

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getFavourite() {
        return mFavourite;
    }

    public void setFavourite(int favourite) {
        mFavourite = favourite;
    }
}