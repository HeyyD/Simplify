package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by hmhat on 7.3.2018.
 */

public class Playlist implements Serializable{

    private String href;
    private String uri;
    private String name;
    private String[] images;

    public Playlist(JSONObject json){
        try {
            this.href = json.getString("href");
            this.name = json.getString("name");
            this.uri = json.getString("uri");

            JSONArray imagesArray = json.getJSONArray("images");
            this.images = new String[imagesArray.length()];

            for(int i = 0; i < imagesArray.length(); i++)
                this.images[i] = imagesArray.getJSONObject(i).getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public String toString(){
        return this.name;
    }
}
