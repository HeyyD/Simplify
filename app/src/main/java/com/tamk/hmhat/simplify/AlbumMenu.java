package com.tamk.hmhat.simplify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The view where the user can see all the albums of the selected
 * artist.
 */

public class AlbumMenu extends Fragment {

    private boolean initialized;
    private MainActivity host;
    private Artist artist;
    private GridLayout albumCoverGrid;
    private ArrayList<Playlist> albums = new ArrayList<>();

    /**
     * Starts the initialization of the view by setting the host (MainActivity)
     * and getting the artist from the arguments of this Fragment.
     * @see Fragment#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.host = (MainActivity) getActivity();
        this.artist = (Artist) getArguments().getSerializable("artist");
        initAlbums();
    }

    /**
     * Creates the UI of this fragment and starts the loading of AlbumButtons.
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.album_menu, container, false);
        TextView textView = v.findViewById(R.id.artist_name);
        textView.setText(artist.getName());
        albumCoverGrid = v.findViewById(R.id.album_list);
        if(initialized)
            createButtons();
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
                        albums.add(new Playlist(jsonArray.getJSONObject(i)));
                    }

                    initialized = true;
                    createButtons();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

    private void createButtons() {

        int buttonSize = (getScreenWidth(host)) / 3;

        for(Playlist album: albums) {
            AlbumButton button = new AlbumButton(host);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = buttonSize;
            params.width = buttonSize;
            button.setLayoutParams(params);

            button.setOnClickListener((listener) -> {
                Fragment fragment = new PlaylistView();
                Bundle args = new Bundle();
                args.putSerializable("playlist", album);

                fragment.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out,
                                                R.anim.fragment_fade_in, R.anim.fragment_fade_out);
                transaction.replace(R.id.main_view, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });

            String albumCover;

            switch (album.getImages().length) {
                case 3: albumCover = album.getImages()[1]; break;
                default: albumCover = album.getImages()[0]; break;
            }

            button.setImage(albumCover);
            albumCoverGrid.addView(button);
        }
    }

    private int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

}
