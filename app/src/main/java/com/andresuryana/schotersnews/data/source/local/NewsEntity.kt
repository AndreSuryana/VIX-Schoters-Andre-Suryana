package com.andresuryana.schotersnews.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.data.model.Source
import com.andresuryana.schotersnews.data.source.local.DBContracts.BookmarkColumns
import com.andresuryana.schotersnews.util.Ext.toDate

@Entity(tableName = BookmarkColumns.TABLE_NAME, indices = [Index(value = [BookmarkColumns.COLUMN_NEWS_URL], unique = true)])
data class NewsEntity(

    @ColumnInfo(name = BookmarkColumns.COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = BookmarkColumns.COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = BookmarkColumns.COLUMN_AUTHOR)
    val author: String? = null,

    @ColumnInfo(name = BookmarkColumns.COLUMN_DESCRIPTION)
    val description: String,

    @ColumnInfo(name = BookmarkColumns.COLUMN_CONTENT)
    val content: String,

    @ColumnInfo(name = BookmarkColumns.COLUMN_SOURCE_ID)
    val sourceId: String?,

    @ColumnInfo(name = BookmarkColumns.COLUMN_SOURCE_NAME)
    val sourceName: String,

    @ColumnInfo(name = BookmarkColumns.COLUMN_NEWS_URL)
    val newsUrl: String,

    @ColumnInfo(name = BookmarkColumns.COLUMN_IMAGE_URL)
    val imageUrl: String?,

    @ColumnInfo(name = BookmarkColumns.COLUMN_PUBLISHED_AT)
    val publishedAt: String
) {
    fun toNews(): News {
        return News(
            source = Source(sourceId, sourceName),
            author = author,
            title = title,
            description = description,
            newsUrl = newsUrl,
            content = content,
            imageUrl = imageUrl,
            publishedAt = publishedAt.toDate()
        )
    }
}
