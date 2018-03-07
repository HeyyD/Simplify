package com.tamk.hmhat.simplify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmhat on 6.3.2018.
 */

public class PlaylistMenu extends Fragment {

    private List<Playlist> playlists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_menu, container, false);
        return v;
    }

    public void initPlaylist(){
        Thread t = new Thread(() -> {
            RequestHandler handler = new RequestHandler((MainActivity) getActivity());
            try {
                JSONObject jsonObject = new JSONObject(handler.makeRequest("https://api.spotify.com/v1/me/playlists", "GET"));
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject o = jsonArray.getJSONObject(i);
                    Playlist playlist = new Playlist(o.getString("id"), o.getString("name"));
                    Log.d(this.getClass().getSimpleName(), playlist.getName());
                    playlists.add(playlist);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}
