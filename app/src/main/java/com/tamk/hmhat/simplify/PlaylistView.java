package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmhat on 8.3.2018.
 */

public class PlaylistView extends Fragment {

    private MainActivity host;
    private int offset = 0;
    private Playlist playlist;
    private ArrayAdapter<Track> adapter;
    private List<Track> tracks = new ArrayList<>();

    private ImageView coverImage;
    private ImageView backgroundImage;
    private String imageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.host = (MainActivity) getActivity();
        this.playlist = (Playlist) getArguments().getSerializable("playlist");
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tracks);

        initSongs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_view, container, false);

        coverImage = v.findViewById(R.id.cover);
        backgroundImage = v.findViewById(R.id.background_image);
        imageUrl = playlist.getImages()[0];

        if(imageUrl != null){
            loadImage(imageUrl, coverImage, backgroundImage);
        }

        ListView listView = v.findViewById(R.id.track_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((list, view, i, l) -> {
            host.getPlayer().playUri(playlist.getUri(), i, 0);
        });

        return v;
    }

    private void loadImage(String url, ImageView cover, ImageView background){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL urlConnection = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                cover.setImageBitmap(result);
                background.setImageBitmap(result);
            }
        };
        task.execute();
    }

    private void initSongs(){

        host.getBuffer().startBuffering();

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                RequestHandler handler = new RequestHandler(host);
                return handler.getMethod(playlist.getHref() + "/tracks?offset=" + offset);
            }

            @Override
            public void onPostExecute(String result){

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject o = jsonArray.getJSONObject(i).getJSONObject("track");
                        tracks.add(new Track(o));
                    }
                    adapter.notifyDataSetChanged();

                    if(jsonArray.length() == 100){
                        offset += jsonArray.length();
                        initSongs();
                    } else {
                        host.getBuffer().stopBuffering(PlaylistView.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        task.execute();
    }

}
