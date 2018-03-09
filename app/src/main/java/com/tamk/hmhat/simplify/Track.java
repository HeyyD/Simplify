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

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
