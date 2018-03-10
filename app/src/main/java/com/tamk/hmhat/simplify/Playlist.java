package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hmhat on 7.3.2018.
 */

public class Playlist implements Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    private String href;
    private String uri;
    private String name;
    private String[] images;

    public Playlist(String href, String uri, String name, String[] images){
        this.href = href;
        this.name = name;
        this.uri = uri;
        this.images = images;
    }

    public Playlist(Parcel in) {
        this.href = in.readString();
        this.name = in.readString();
        this.uri = in.readString();
        this.images = in.createStringArray();
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String[] getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public String toString(){
        return this.name;
    }
}
