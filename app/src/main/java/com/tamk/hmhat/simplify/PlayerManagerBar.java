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

    private MainActivity host;
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

        host = (MainActivity) getActivity();

        return v;
    }

    private void previous(){
        host.getPlayer().skipToPrevious();
    }

    private void play(){

        if(host.getPlayer().getPlaybackState().isPlaying)
            host.getPlayer().pause();
        else
            host.getPlayer().resume();
    }

    private void next() {
        host.getPlayer().skipToNext();
    }

    public void setCurrentTrack(String artist, String track){
        currentTrack.setText(track + " - " + artist);
    }
}
