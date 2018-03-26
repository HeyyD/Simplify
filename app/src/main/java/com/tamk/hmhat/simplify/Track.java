package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hmhat on 9.3.2018.
 */

public class Track {

    private String name;
    private Artist[] artists;

    public Track(JSONObject json) {
        try {
            this.name = json.getString("name");

            JSONArray jsonArray = json.getJSONArray("artists");
            this.artists = new Artist[jsonArray.length()];

            for(int i = 0; i < jsonArray.length(); i++){
                this.artists[i] = new Artist(jsonArray.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Artist[] getArtists(){
        return this.artists;
    }

    @Override
    public String toString() {

        String string = this.name + " -";

        for(Artist artist: this.artists)
            string += (" " + artist.toString());

        return string;
    }
}
