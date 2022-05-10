package com.masterlibs.basestructure.utils;

import java.io.Serializable;

public class MyFile implements Serializable {
    private String path;

    public MyFile(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}

