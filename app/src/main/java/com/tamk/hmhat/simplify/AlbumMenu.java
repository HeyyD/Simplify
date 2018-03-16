package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hmhat on 15.3.2018.
 */

public class AlbumMenu extends Fragment {

    private MainActivity host;
    private Artist artist;

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
                Log.d("DEBUG", result);
            }
        };
        task.execute();
    }
}
