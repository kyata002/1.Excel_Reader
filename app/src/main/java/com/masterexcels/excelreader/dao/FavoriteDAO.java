package com.masterexcels.excelreader.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.masterexcels.excelreader.model.MyFile;

import java.util.List;
@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM file_table ORDER BY id DESC")
    List<MyFile> getList();

    @Query("SELECT * FROM file_table WHERE path=:path")
    MyFile getFile(String path);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(MyFile myFile);

    @Update
    void update(MyFile myFile);

    @Query("DELETE FROM file_table WHERE path =:path")
    void delete(String path);

    @Query("DELETE FROM file_table ")
    void deleteAll();
}
