package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hmhat on 15.3.2018.
 */

public class AlbumMenu extends Fragment {

    private MainActivity host;
    private Artist artist;
    private ArrayList<Album> albums = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.host = (MainActivity) getActivity();
        this.artist = (Artist) getArguments().getSerializable("artist");
        initAlbums();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.album_menu, container, false);
        TextView textView = v.findViewById(R.id.artist_name);
        textView.setText(artist.getName());
        return v;
    }

    private void initAlbums(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String,Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                RequestHandler handler = new RequestHandler(host);
                return handler.getMethod(artist.getHref() + "/albums");
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        albums.add(new Album(jsonArray.getJSONObject(i)));
                    }

                    Log.d("DEBUG", "albums: " + albums.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }
}
