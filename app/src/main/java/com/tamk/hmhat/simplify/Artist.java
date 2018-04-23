package com.tamk.hmhat.simplify;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Artist class stores all the needed information from Spotify artist.
 */

public class Artist implements Comparable<Artist>, Serializable {

    private String href;
    private String name;

    /**
     * The constructor parses the needed information from the JSON data fetched from
     * the API.
     * @param json Fetched information
     */
    public Artist(JSONObject json) {
        try {
            this.href = json.getString("href");
            this.name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Name of the artist
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Link that is used to fetch information about the artist
     */
    public String getHref() {return this.href;}

    /**
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(@NonNull Artist artist) {
        return this.name.compareTo(artist.getName());
    }

    /**
     * @see Object#toString() 
     */
    @Override
    public String toString(){
        return this.name;
    }
}
