package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Fragment that lists all the artist for users selected playlist.
 */

public class ArtistList extends Fragment {

    private MainActivity host;
    private Playlist playlist;
    private List<Artist> artists = new ArrayList<>();
    private Set<Artist> artistsSet = new TreeSet<>();
    private ArrayAdapter<Artist> adapter;

    private int offset;

    /**
     * Initializes the fragment by setting the host of the fragment (MainActivity)
     * and getting the playlist which has the artists that we want to list from the
     * fragments arguments.
     * @see Fragment#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.playlist = (Playlist) getArguments().getSerializable("playlist");
        this.host = (MainActivity) getActivity();
        initArtists();
    }

    /**
     * Creates the UI for the list of artists. Also creates a itemClickListener for the
     * list.
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.artist_list, container, false);
        TextView textView = v.findViewById(R.id.playlist_name);
        textView.setText(playlist.getName());

        ListView artistList = v.findViewById(R.id.artist_list);
        adapter = new ArrayAdapter<>(host, android.R.layout.simple_list_item_1, artists);
        artistList.setAdapter(adapter);

        artistList.setOnItemClickListener((list, view, i, l) -> {
            Artist artist = (Artist) list.getItemAtPosition(i);
            changeFragment(artist);
        });

        return v;
    }

    private void changeFragment(Artist artist){
        Fragment albumMenu = new AlbumMenu();
        Bundle args = new Bundle();
        args.putSerializable("artist", artist);
        albumMenu.setArguments(args);

        FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out,
                                        R.anim.fragment_fade_in, R.anim.fragment_fade_out);

        transaction.replace(R.id.main_view, albumMenu);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void initArtists() {

        host.getBuffer().startBuffering();

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
                            artistsSet.add(new Artist(artists.getJSONObject(j)));
                        }

                    }

                    offset += jsonArray.length();

                    if(jsonArray.length() == 100) {
                        initArtists();
                    } else {
                        artists.addAll(artistsSet);
                        adapter.notifyDataSetChanged();
                        host.getBuffer().stopBuffering(ArtistList.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

}
