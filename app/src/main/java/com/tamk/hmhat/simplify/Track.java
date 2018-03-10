package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hmhat on 9.3.2018.
 */

public class Track implements Parcelable{
    
    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    private String uri;
    private String name;

    public Track(String uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    protected Track(Parcel in) {
        uri = in.readString();
        name = in.readString();
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
