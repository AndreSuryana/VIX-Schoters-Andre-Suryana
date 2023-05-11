package com.andresuryana.schotersnews.data.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.andresuryana.schotersnews.data.model.News

class NewsDiffUtil(
    private val newList: List<News>,
    private val oldList: List<News>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].newsUrl == oldList[oldItemPosition].newsUrl

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]
}