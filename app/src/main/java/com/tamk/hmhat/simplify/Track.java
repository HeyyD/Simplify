package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Track class holds all the needed information that the application uses.
 */

public class Track {

    private String name;
    private Artist[] artists;

    /**
     * The constructor parses all the needed information from the given JSON
     * @param json data that needs to be parsed
     */
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

    /**
     * @return Name of the track
     */
    public String getName() {
        return name;
    }

    /**
     * @return Array of artists of the song
     */
    public Artist[] getArtists(){
        return this.artists;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {

        String string = this.name + " -";

        for(Artist artist: this.artists)
            string += (" " + artist.toString());

        return string;
    }
}
