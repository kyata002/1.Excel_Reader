package com.masterlibs.basestructure.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.masterlibs.basestructure.dao.HistoryDao;
import com.masterlibs.basestructure.model.FileModel;


@Database(entities = {FileModel.class}, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract HistoryDao historyDao();

    private static RoomDatabase instance;

    public static RoomDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomDatabase.class, "my_db").allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
