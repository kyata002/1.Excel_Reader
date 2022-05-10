package com.masterlibs.basestructure.utils;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "file_table")
public class MyFile implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

