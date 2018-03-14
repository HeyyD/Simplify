package com.tamk.hmhat.simplify;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FrameMetricsAggregator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private Player player;
    private PlayerManagerBar playerManager;

    private static final String CLIENT_ID = "fed18b1e630343c2bbd7d05000b2c2a8";
    private static final String REDIRECT_URI = "http://localhost:8888/callback/";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private static String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initPlaylist();

        playerManager = (PlayerManagerBar) getSupportFragmentManager().findFragmentById(R.id.player_manager_bar);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        initBuffer();
    }

    private void initPlaylist(){
        PlaylistMenu fragment = new PlaylistMenu();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_view, fragment);
        transaction.commit();
    }

    private void initBuffer(){
        Fragment fragment = new Buffer();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_view, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                this.accessToken = response.getAccessToken();
                Log.d("ACCESS_TOKEN", response.getAccessToken());
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

        FragmentManager fm = getSupportFragmentManager();
        for(Fragment fragment: fm.getFragments()){
            if(fragment instanceof PlaylistMenu){
                ((PlaylistMenu) fragment).initPlaylist();
            }
        }
    }

    @Override
    public void onLoggedOut() {
        Log.d(this.getClass().getSimpleName(), "onLoggedOut");
    }

    @Override
    public void onLoginFailed(int i) {
        Log.d(this.getClass().getSimpleName(), "onLoginFailed: " + i);
        //9 = no premium account (I can't find any enum class for the
        //Spotify sdk errors so I just simply have to use the raw integer
        if(i == 9) {
            Log.d(this.getClass().getSimpleName(), "No Premium");
            //Notify the user that they need a premium account
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NOT A PREMIUM ACCOUNT")
                    .setMessage("Spotify account must be upgraded to premium");
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setOnDismissListener((d) -> {
                Log.d(this.getClass().getSimpleName(), "DISMISS");
                this.finish();
            });
        }
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

        if(playerEvent == PlayerEvent.kSpPlaybackNotifyTrackChanged){
            String artist = player.getMetadata().currentTrack.artistName;
            String track = player.getMetadata().currentTrack.name;

            playerManager.setCurrentTrack(artist, track);

            if(!player.getPlaybackState().isPlaying)
                playerManager.init();

            if(!player.getPlaybackState().isShuffling)
                player.setShuffle(true);
        }

    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(this.getClass().getSimpleName(), "onPlaybackError");
    }

    public String getAccessToken() {return this.accessToken;}
    public Player getPlayer() {return this.player;}
}
