package com.simplify.hmhat.simplify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private static final String CLIENT_ID = "fed18b1e630343c2bbd7d05000b2c2a8";
    private static final String REDIRECT_URI = "localhost:8080/callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(this.getClass().getSimpleName(), "onCreate");
    }

    @Override
    public void onLoggedIn() {
        Log.d(this.getClass().getSimpleName(), "onLoggedIn");
    }

    @Override
    public void onLoggedOut() {
        Log.d(this.getClass().getSimpleName(), "onLoggedOut");
    }

    @Override
    public void onLoginFailed(int i) {
        Log.d(this.getClass().getSimpleName(), "onLoggedFailed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(this.getClass().getSimpleName(), "onTemporaryError");
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(this.getClass().getSimpleName(), "onConnectionMessage");
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(this.getClass().getSimpleName(), "onPlaybackEvent");
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(this.getClass().getSimpleName(), "onPlaybackError");
    }
}
