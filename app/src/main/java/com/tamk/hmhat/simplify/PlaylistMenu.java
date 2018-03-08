package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ArrayAdapter<Playlist> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_menu, container, false);
        ListView listView = v.findViewById(R.id.user_lists);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, playlists);
        listView.setAdapter(adapter);
        return v;
    }

    public void initPlaylist(){
        RequestHandler handler = new RequestHandler((MainActivity) getActivity());

        //AsyncTask will get the playlist's in a thread and update the view post execute
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return handler.getMethod("https://api.spotify.com/v1/me/playlists");
            }

            @Override
            protected void onPostExecute(String result){
                try {
                    JSONObject jsonObject = new JSONObject(result);
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
                adapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute();
    }
}
