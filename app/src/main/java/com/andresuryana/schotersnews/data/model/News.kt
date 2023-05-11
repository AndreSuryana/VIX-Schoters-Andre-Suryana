package com.andresuryana.schotersnews.data.model

import android.os.Parcelable
import com.andresuryana.schotersnews.data.source.local.NewsEntity
import com.andresuryana.schotersnews.util.Ext.formatDate
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class News(

    @SerializedName("source")
    val source: Source,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("url")
    val newsUrl: String,

    @SerializedName("urlToImage")
    val imageUrl: String?,

    @SerializedName("publishedAt")
    val publishedAt: Date
) : Parcelable {
    fun toNewsEntity(): NewsEntity {
        return NewsEntity(
            title = title,
            author = author,
            description = description,
            content = content,
            sourceId = source.id,
            sourceName = source.name,
            newsUrl = newsUrl,
            imageUrl = imageUrl,
            publishedAt = publishedAt.formatDate()
        )
    }
}
