package com.tamk.hmhat.simplify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Manages the buffering view between Fragment changes.
 */

public class Buffer {

    private MainActivity host;
    private boolean buffering = false;

    /**
     * Sets the MainActivity as the host of the buffer.
     * @param host MainActivity of the software
     */
    public Buffer (MainActivity host) {
        this.host = host;
    }

    /**
     * Replaces the previous fragment to BufferWindow. BufferWindow
     * is shown between loading of the fragments.
     */
    public void startBuffering() {
        if(!buffering){
            BufferWindow buffer = new BufferWindow();
            FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, buffer);
            transaction.commit();
            buffering = true;
        }
    }

    /**
     * Replaces the BufferWindow with the next fragment.
     * @param nextFragment Fragment to be shown next
     */
    public void stopBuffering(Fragment nextFragment) {
        if(buffering){
            FragmentTransaction transaction = host.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, nextFragment);
            transaction.commit();
            buffering = false;
        }
    }

    /**
     * @return Is the application currently buffering
     */
    public boolean isBuffering() {return buffering;}

}
