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
 * Created by hmhat on 8.3.2018.
 */

public class PlaylistView extends Fragment {

    private String href;
    private ArrayAdapter<Track> adapter;
    private List<Track> tracks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_view, container, false);
        this.href = getArguments().getString("href");

        ListView listView = v.findViewById(R.id.track_list);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tracks);
        listView.setAdapter(adapter);
        initSongs();
        return v;
    }

    private void initSongs(){
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                RequestHandler handler = new RequestHandler((MainActivity) getActivity());
                return handler.getMethod(href + "/tracks");
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        task.execute();
    }

}
