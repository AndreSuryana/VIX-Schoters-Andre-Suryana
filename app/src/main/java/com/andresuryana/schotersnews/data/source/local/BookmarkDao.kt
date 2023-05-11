package com.andresuryana.schotersnews.data.source.local

import androidx.room.*
import com.andresuryana.schotersnews.data.source.local.DBContracts.BookmarkColumns

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM ${BookmarkColumns.TABLE_NAME} ORDER BY ${BookmarkColumns.COLUMN_ID} DESC")
    fun getAllNews(): List<NewsEntity>

    @Insert(NewsEntity::class)
    fun insert(news: NewsEntity)

    @Query("DELETE FROM ${BookmarkColumns.TABLE_NAME} WHERE ${BookmarkColumns.COLUMN_NEWS_URL} = :url")
    fun remove(url: String)

    @Query("SELECT EXISTS(SELECT * FROM ${BookmarkColumns.TABLE_NAME} WHERE ${BookmarkColumns.COLUMN_NEWS_URL} = :url)")
    fun isNewsBookmarked(url: String): Boolean
}