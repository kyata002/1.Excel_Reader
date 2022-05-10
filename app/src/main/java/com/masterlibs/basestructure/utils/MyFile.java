package com.masterlibs.basestructure.utils;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class MyFile implements Serializable {
    private String path;
    private boolean isFavorite = false;

    public MyFile(String path) {
        this.path = path;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}

