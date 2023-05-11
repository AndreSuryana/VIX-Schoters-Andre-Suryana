package com.andresuryana.schotersnews.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class SchotersNewsDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
}