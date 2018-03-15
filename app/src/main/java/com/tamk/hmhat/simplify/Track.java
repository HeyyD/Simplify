package com.tamk.hmhat.simplify;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hmhat on 9.3.2018.
 */

public class Track {

    private String name;

    public Track(JSONObject json) {
        try {
            this.name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
