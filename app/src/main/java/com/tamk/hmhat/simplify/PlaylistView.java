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
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmhat on 8.3.2018.
 */

public class PlaylistView extends Fragment {

    private int offset = 0;
    private String href;
    private ArrayAdapter<Track> adapter;
    private List<Track> tracks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.href = getArguments().getString("href");
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tracks);
        initSongs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_view, container, false);

        String imageUrl = getArguments().getStringArray("images")[0];

        if(imageUrl != null){
            ImageView imageView = v.findViewById(R.id.cover);
            ImageLoadTask loadImage = new ImageLoadTask(imageUrl, imageView);
            loadImage.execute();
        }
        
        ListView listView = v.findViewById(R.id.track_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((list, view, i, l) -> {
            Track track = (Track) list.getItemAtPosition(i);
            MainActivity.player.playUri(track.getUri(), 0, 0);
        });

        return v;
    }

    private void initSongs(){
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                RequestHandler handler = new RequestHandler((MainActivity) getActivity());
                return handler.getMethod(href + "/tracks?offset=" + offset);
            }

            @Override
            public void onPostExecute(String result){

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject o = jsonArray.getJSONObject(i).getJSONObject("track");
                        String uri = o.getString("uri");
                        String name = o.getString("name");
                        tracks.add(new Track(uri, name));
                    }
                    adapter.notifyDataSetChanged();

                    if(jsonArray.length() == 100){
                        offset += jsonArray.length();
                        initSongs();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        task.execute();
    }

}
