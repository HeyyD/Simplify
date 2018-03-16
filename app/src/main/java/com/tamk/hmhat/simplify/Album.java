package com.tamk.hmhat.simplify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hmhat on 16.3.2018.
 */

public class Album {

    private String name;

    public Album(JSONObject json){
        try {
            this.name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.name;
    }
}
