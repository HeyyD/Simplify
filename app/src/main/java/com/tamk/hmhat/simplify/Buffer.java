package com.tamk.hmhat.simplify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by hmhat on 14.3.2018.
 */

public class Buffer {

    private MainActivity host;
    private static boolean isBuffering = false;

    public Buffer (MainActivity host) {
        this.host = host;
    }

    public void startBuffering() {
        if(!isBuffering){
            BufferWindow buffer = new BufferWindow();
            FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, buffer);
            transaction.commit();
            isBuffering = true;
        }
    }

    public void stopBuffering(Fragment nextFragment) {
        if(isBuffering){
            FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, nextFragment);
            transaction.commit();
            isBuffering = false;
        }
    }

}
