package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by hmhat on 14.3.2018.
 */

public class ArtistList extends Fragment {

    private MainActivity host;
    private Playlist playlist;
    private Set<Artist> set = new TreeSet<>();

    private int offset;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.playlist = getArguments().getParcelable("playlist");
        this.host = (MainActivity) getActivity();
        initArtists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.artist_list, container, false);
        TextView textView = v.findViewById(R.id.playlist_name);
        textView.setText(playlist.getName());
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
                            set.add(new Artist(artists.getJSONObject(j)));
                        }

                    }

                    offset += jsonArray.length();

                    if(jsonArray.length() == 100) {
                        initArtists();
                    } else {
                        Log.d("DEBUG", set.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

}
