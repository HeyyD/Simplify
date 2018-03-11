package com.tamk.hmhat.simplify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.sdk.android.player.Player;

/**
 * Created by hmhat on 6.3.2018.
 */

public class PlayerManagerBar extends Fragment {

    private MainActivity host;
    private TextView currentTrack;
    ImageButton playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.player_manager, container, false);

        ImageButton previous = v.findViewById(R.id.previous);
        previous.setOnClickListener((l) -> previous());

        playButton = v.findViewById(R.id.play);
        playButton.setOnClickListener((l) -> play());

        ImageButton next = v.findViewById(R.id.next);
        next.setOnClickListener((l) -> next());

        currentTrack = v.findViewById(R.id.currentTrack);

        host = (MainActivity) getActivity();

        return v;
    }

    private void previous(){
        if(host.getPlayer().getMetadata().prevTrack != null)
            host.getPlayer().skipToPrevious();
        else
            host.getPlayer().seekToPosition(0);
    }

    private void play(){

        if(host.getPlayer().getPlaybackState().isPlaying){
            host.getPlayer().pause();
            playButton.setImageDrawable(getResources().getDrawable(R.mipmap.play_button));
        }
        else{
            host.getPlayer().resume();
            playButton.setImageDrawable(getResources().getDrawable(R.mipmap.pause_button));
        }
    }

    private void next() {
        if(host.getPlayer().getMetadata().nextTrack != null)
            host.getPlayer().skipToNext();
    }

    public void setCurrentTrack(String artist, String track){
        currentTrack.setText(track + " - " + artist);
    }
}
