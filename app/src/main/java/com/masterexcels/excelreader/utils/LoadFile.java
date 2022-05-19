package com.masterexcels.excelreader.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class LoadFile {
    public static ArrayList<File> loadFile(Context context, String type) {
        ArrayList<File> list = new ArrayList<>();
        Uri table = MediaStore.Files.getContentUri("external");
        String selection = "_data LIKE '%." + type + "'";

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(table, null, selection, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (cursor == null || cursor.getCount() <= 0 || !cursor.moveToFirst()) {
                // this means error, or simply no results found
                return list;
            }
            int data = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);

            do {
                String path = cursor.getString(data);
                File file = new File(path);
                if (file.length() == 0) {
                    continue;
                }
                list.add(file);
            }
            while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}
