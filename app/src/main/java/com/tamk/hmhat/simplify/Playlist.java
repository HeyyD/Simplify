package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Playlist class holds the relevant information that the application needs from the playlist's
 * data.
 */

public class Playlist implements Serializable{

    private String href;
    private String uri;
    private String name;
    private String[] images;

    /**
     * The constructor parses all the needed data from the playlist data and saves it into
     * attributes.
     * @param json JSON that holds the playlist data.
     */
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

    /**
     * @return Link to the data of the playlist.
     */
    public String getHref() {
        return href;
    }

    /**
     * @return Name of the playlist
     */
    public String getName() {
        return name;
    }

    /**
     * @return Uri that is used by the music player when the user wants to listen to the playlist.
     */
    public String getUri() {
        return uri;
    }

    /**
     * @return Array of links that hold the cover images of the playlist
     */
    public String[] getImages() {
        return images;
    }

    /**
     * @return Name of the playlist
     */
    @Override
    public String toString(){
        return this.name;
    }
}
