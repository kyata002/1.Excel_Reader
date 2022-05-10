package com.masterlibs.basestructure.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.masterlibs.basestructure.model.FileModel;
import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM file_table ORDER BY _id DESC")
    List<FileModel> getList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(FileModel fileModel);

    @Query("DELETE FROM file_table WHERE _id =:id")
    void delete(int id);

    @Query("DELETE FROM file_table ")
    void deleteAll();
}
