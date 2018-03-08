package com.tamk.hmhat.simplify;

/**
 * Created by hmhat on 7.3.2018.
 */

public class Playlist {

    private String id;
    private String name;

    public Playlist(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
