package com.tamk.hmhat.simplify;

/**
 * Created by hmhat on 7.3.2018.
 */

public class Playlist {

    private String href;
    private String name;
    private String[] images;

    public Playlist(String href, String name, String[] images){
        this.href = href;
        this.name = name;
        this.images = images;
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }


    public String[] getImages() {
        return images;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
