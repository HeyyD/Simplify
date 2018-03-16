package com.tamk.hmhat.simplify;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by hmhat on 15.3.2018.
 */

public class Artist implements Comparable<Artist>, Serializable {

    private String name;

    public Artist(JSONObject json) {
        try {
            this.name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(@NonNull Artist artist) {
        return this.name.compareTo(artist.getName());
    }

    @Override
    public String toString(){
        return this.name;
    }
}
