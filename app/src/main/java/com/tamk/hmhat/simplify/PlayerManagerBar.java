package com.tamk.hmhat.simplify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.sdk.android.player.Player;

/**
 * Created by hmhat on 6.3.2018.
 */

public class PlayerManagerBar extends Fragment {

    private TextView currentTrack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_manager, container, false);

        Button previous = v.findViewById(R.id.previous);
        previous.setOnClickListener((l) -> previous());

        Button play = v.findViewById(R.id.play);
        play.setOnClickListener((l) -> play());

        Button next = v.findViewById(R.id.next);
        next.setOnClickListener((l) -> next());

        currentTrack = v.findViewById(R.id.currentTrack);

        return v;
    }

    private void previous(){
        MainActivity.player.skipToPrevious();
    }

    private void play(){
        Log.d("DEBUG", MainActivity.player.getPlaybackState().toString());

        if(MainActivity.player.getPlaybackState().isPlaying)
            MainActivity.player.pause();
        else
            MainActivity.player.resume();
    }

    private void next() {
        MainActivity.player.skipToNext();
    }

    public void setCurrentTrack(String artist, String track){
        currentTrack.setText(track + " - " + artist);
    }
}
