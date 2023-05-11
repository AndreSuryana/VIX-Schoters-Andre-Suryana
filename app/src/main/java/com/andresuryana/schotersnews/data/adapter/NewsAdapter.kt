package com.andresuryana.schotersnews.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.ItemNewsHorizontalBinding
import com.andresuryana.schotersnews.data.adapter.diffutil.NewsDiffUtil
import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.util.Ext.formatDateToDaysAgo
import com.bumptech.glide.Glide

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var list: List<News> = emptyList()
    private var onItemClickListener: ((news: News) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setList(list: List<News>) {
        val diffResult = DiffUtil.calculateDiff(NewsDiffUtil(list, this.list))
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickListener(onItemClickListener: (news: News) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemNewsHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(news: News) {
            // Assign data
            binding.apply {
                Glide.with(root)
                    .load(news.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(image)
                title.text = news.title
                author.text = news.author ?: "Anonymous"
                date.text = news.publishedAt.formatDateToDaysAgo()
            }

            // Item click listener
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(news)
            }
        }
    }
}