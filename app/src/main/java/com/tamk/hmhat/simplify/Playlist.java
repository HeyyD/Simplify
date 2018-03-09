package com.tamk.hmhat.simplify;

/**
 * Created by hmhat on 7.3.2018.
 */

public class Playlist {

    private String href;
    private String name;

    public Playlist(String href, String name){
        this.href = href;
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
