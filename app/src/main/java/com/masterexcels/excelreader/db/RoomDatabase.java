package com.masterexcels.excelreader.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.masterexcels.excelreader.dao.FavoriteDAO;
import com.masterexcels.excelreader.dao.HistoryDao;
import com.masterexcels.excelreader.model.MyFile;


@Database(entities = {MyFile.class}, version = 2, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract HistoryDao historyDao();

    public abstract FavoriteDAO favoriteDAO();

    private static RoomDatabase instance;

    public static RoomDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomDatabase.class, "excel_db").allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
