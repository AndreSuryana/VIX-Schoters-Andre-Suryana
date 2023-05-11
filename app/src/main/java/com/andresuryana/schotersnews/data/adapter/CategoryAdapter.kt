package com.andresuryana.schotersnews.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.ItemCategoryBinding
import com.andresuryana.schotersnews.util.NewsCategory
import com.bumptech.glide.Glide

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var list = NewsCategory.values().toList()
    private var onItemClickListener: ((newsCategory: NewsCategory) -> Unit)? = null
    private var selectedCategory: NewsCategory = NewsCategory.values().first()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickListener(onItemClickListener: (news: NewsCategory) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedNewsCategory(newsCategory: NewsCategory) {
        val oldSelectedCategory = this.selectedCategory
        this.selectedCategory = newsCategory

        notifyItemChanged(getItemPosition(oldSelectedCategory))
        notifyItemChanged(getItemPosition(newsCategory))
    }

    private fun getItemPosition(newsCategory: NewsCategory): Int =
        this.list.indexOf(newsCategory)

    inner class ViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(newsCategory: NewsCategory) {
            // Assign data
            binding.apply {
                Glide.with(root)
                    .load(newsCategory.imageRes)
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(image)
                title.text = newsCategory.name

                // Check selected news category
                binding.image.strokeWidth = if (newsCategory == selectedCategory) 8f else 0f
            }

            // Item click listener
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(newsCategory)
            }
        }
    }
}