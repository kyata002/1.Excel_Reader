package com.masterexcels.excelreader.model;

import androidx.room.Entity;

@Entity(tableName = "file_recent")
public class FileRecent extends MyFile{
    public FileRecent(String path) {
        super(path);
    }

}
