package com.masterlibs.basestructure.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.masterlibs.basestructure.dao.FavoriteDAO;
import com.masterlibs.basestructure.dao.HistoryDao;
import com.masterlibs.basestructure.model.MyFile;


@Database(entities = {MyFile.class}, version = 2, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract HistoryDao historyDao();
    public abstract FavoriteDAO favoriteDAO();
    private static RoomDatabase instance;

    public static RoomDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomDatabase.class, "my_db").allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
