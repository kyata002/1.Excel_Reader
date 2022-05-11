package com.masterlibs.basestructure.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.masterlibs.basestructure.model.MyFile;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM file_table ORDER BY id DESC")
    List<MyFile> getList();

    @Query("SELECT * FROM file_table WHERE path=:path")
    MyFile getFileModel(String path);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(MyFile fileModel);

    @Query("DELETE FROM file_table WHERE id =:id")
    void delete(int id);

    @Query("DELETE FROM file_table ")
    void deleteAll();


}
