package com.tamk.hmhat.simplify;

/**
 * Created by hmhat on 9.3.2018.
 */

public class Track {

    private String uri;
    private String name;

    public Track(String uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
