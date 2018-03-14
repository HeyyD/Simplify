package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmhat on 6.3.2018.
 */

public class PlaylistMenu extends Fragment {

    private MainActivity host;
    private List<Playlist> playlists = new ArrayList<>();
    private ArrayAdapter<Playlist> adapter;

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.host = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_menu, container, false);

        ListView listView = v.findViewById(R.id.user_lists);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, playlists);
        listView.setAdapter(adapter);

        gestureDetector = new GestureDetector(host, new ListGestureListener(listView));

        listView.setOnItemClickListener((list, view, i, l) -> {
            Playlist playlist = (Playlist) list.getItemAtPosition(i);
            changeFragment(playlist, new PlaylistView());
        });

        listView.setOnTouchListener(((view, event) -> {
            return gestureDetector.onTouchEvent(event);
        }));

        return v;
    }

    private void changeFragment(Playlist playlist, Fragment fragment){
        Bundle args = new Bundle();
        args.putParcelable("playlist", playlist);

        fragment.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit,
                                        R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.main_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void initPlaylist(){
        host.getBuffer().startBuffering();
        RequestHandler handler = new RequestHandler(host);

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
                        String href = o.getString("href");
                        String name = o.getString("name");
                        String uri = o.getString("uri");

                        JSONArray imagesJson = o.getJSONArray("images");
                        String[] imageUrls = new String[imagesJson.length()];

                        for(int j = 0; j < imagesJson.length(); j++){
                            imageUrls[j] = imagesJson.getJSONObject(j).getString("url");
                        }

                        Playlist playlist = new Playlist(href, uri, name, imageUrls);
                        playlists.add(playlist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                host.getBuffer().stopBuffering(PlaylistMenu.this);
            }
        };
        asyncTask.execute();
    }

    private class ListGestureListener extends GestureDetector.SimpleOnGestureListener {

        private ListView listView;

        public ListGestureListener(ListView listView) {
            this.listView = listView;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            try{
                Playlist playlist = adapter.getItem(listView.pointToPosition(Math.round(event1.getX()),
                                                                             Math.round(event1.getY())));
                Log.d("DEBUG", playlist.toString());
                changeFragment(playlist, new ArtistList());
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            return true;
        }
    }
}
