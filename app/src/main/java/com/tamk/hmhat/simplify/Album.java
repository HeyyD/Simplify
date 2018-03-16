package com.tamk.hmhat.simplify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hmhat on 16.3.2018.
 */

public class Album {

    private String name;
    private String[] images;

    public Album(JSONObject json){
        try {
            this.name = json.getString("name");

            JSONArray jsonImages = json.getJSONArray("images");
            this.images = new String[jsonImages.length()];
            for(int i = 0; i < jsonImages.length(); i++) {
                this.images[i] = jsonImages.getJSONObject(i).getString("url");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.name;
    }
}
