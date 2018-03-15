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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by hmhat on 14.3.2018.
 */

public class ArtistList extends Fragment {

    private MainActivity host;
    private Playlist playlist;
    private Set<Artist> artists = new TreeSet<>();
    private ArrayAdapter<Artist> adapter;

    private int offset;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.playlist = (Playlist) getArguments().getSerializable("playlist");
        this.host = (MainActivity) getActivity();
        initArtists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.artist_list, container, false);
        TextView textView = v.findViewById(R.id.playlist_name);
        textView.setText(playlist.getName());

        ListView list = v.findViewById(R.id.artist_list);
        adapter = new ArrayAdapter<>(host, android.R.layout.simple_list_item_1, new ArrayList<>());
        list.setAdapter(adapter);
        
        return v;
    }

    private void initArtists() {
        @SuppressLint("StaticFieldLeak") AsyncTask<String,Void,String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return new RequestHandler(host).getMethod(playlist.getHref() + "/tracks?offset=" + offset);
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject track = jsonArray.getJSONObject(i).getJSONObject("track");
                        JSONArray artists = track.getJSONArray("artists");

                        for(int j = 0; j < artists.length(); j++) {
                            ArtistList.this.artists.add(new Artist(artists.getJSONObject(j)));
                        }

                    }

                    offset += jsonArray.length();

                    if(jsonArray.length() == 100) {
                        initArtists();
                    } else {
                        adapter.addAll(artists);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

}
