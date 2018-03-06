package com.simplify.hmhat.simplify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private static final String CLIENT_ID = "fed18b1e630343c2bbd7d05000b2c2a8";
    private static final String REDIRECT_URI = "http://localhost:8888/callback/";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        player = spotifyPlayer;
                        player.addConnectionStateCallback(MainActivity.this);
                        player.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {
        Log.d(this.getClass().getSimpleName(), "onLoggedIn");
        //player.playUri("spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
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
