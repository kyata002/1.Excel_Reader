package com.masterlibs.basestructure.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file_table")
class FileModel(var id: String, var path: String) {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}