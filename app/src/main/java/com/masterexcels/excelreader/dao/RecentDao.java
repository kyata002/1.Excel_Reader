package com.masterexcels.excelreader.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.masterexcels.excelreader.model.FileRecent;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecentDao {
    @Query("SELECT * FROM file_recent ORDER BY id DESC")
    List<FileRecent> getList();

    @Query("SELECT * FROM file_recent WHERE path=:path")
    boolean hasFile(String path);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(FileRecent myFile);

    @Update
    void update(FileRecent myFile);

    @Query("DELETE FROM file_recent WHERE path =:path")
    void delete(String path);

    @Query("DELETE FROM file_recent ")
    void deleteAll();
}
