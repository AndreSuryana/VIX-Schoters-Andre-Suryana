package com.andresuryana.schotersnews.util

import android.content.Intent
import android.os.Parcelable
import com.andresuryana.schotersnews.data.model.ResponseWrapper
import com.google.gson.Gson
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

object Ext {

    fun String.toDate(): Date {
        val formatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return formatter.parse(this)!!
    }

    fun Date.formatDate(pattern: String = DATE_TIME_FORMAT): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(this)
    }

    fun Date.formatDateToDaysAgo(): String {
        val now = Date()
        val diff = now.time - this.time

        return when (val days = diff / (1000 * 60 * 60 * 24)) {
            0L -> "Today"
            1L -> "Yesterday"
            in 2..6 -> "$days days ago"
            else -> SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(this)
        }
    }

    fun Response<ResponseWrapper>.parseErrorResponse(): ResponseWrapper {
        val errorBody = errorBody()?.string().toString()
        return Gson().fromJson(errorBody, ResponseWrapper::class.java)
    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Parcelable> Intent.getParcelableExtraOrNull(name: String): T? {
        return getParcelableExtra(name)
    }
}