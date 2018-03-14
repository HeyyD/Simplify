package com.tamk.hmhat.simplify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by hmhat on 14.3.2018.
 */

public class Buffer {

    private MainActivity host;
    private boolean buffering = false;

    public Buffer (MainActivity host) {
        this.host = host;
    }

    public void startBuffering() {
        if(!buffering){
            BufferWindow buffer = new BufferWindow();
            FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, buffer);
            transaction.commit();
            buffering = true;
        }
    }

    public void stopBuffering(Fragment nextFragment) {
        if(buffering){
            FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, nextFragment);
            transaction.commit();
            buffering = false;
        }
    }

    public void cancel() {

    }

    public boolean isBuffering() {return buffering;}

}
