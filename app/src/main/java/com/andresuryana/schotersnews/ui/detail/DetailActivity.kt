package com.andresuryana.schotersnews.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.ActivityDetailBinding
import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.util.Ext.formatDateToDaysAgo
import com.andresuryana.schotersnews.util.Ext.getParcelableExtraOrNull
import com.andresuryana.schotersnews.util.SnackbarHelper.showErrorSnackbar
import com.andresuryana.schotersnews.util.SnackbarHelper.showSuccessSnackbar
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel>()

    private var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from bundle
        news = intent.getParcelableExtraOrNull(EXTRA_NEWS)
        news?.let { setNews(it) }

        // Observe ui state
        observeUiState()

        // Setup button
        setupBookmarkButton()
        setupButtonBack()
        setupButtonShare()
    }

    private fun setupBookmarkButton() {
        binding.btnBookmark.setOnCheckedChangeListener { _, isChecked ->
            news?.let {
                viewModel.updateBookmarkArticle(it, isChecked)
                if (isChecked) showSuccessSnackbar(binding.root, getString(R.string.insert_bookmark))
                else showErrorSnackbar(binding.root, getString(R.string.remove_bookmark))
            }
        }
    }

    private fun setupButtonBack() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupButtonShare() {
        binding.btnShare.setOnClickListener {
            if (news != null) {
                val intent = Intent.createChooser(
                    Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            getString(R.string.share_news, news?.title, news?.newsUrl)
                        )
                        type = "text/plain"
                    },
                    getString(R.string.title_share_news)
                )
                startActivity(intent)
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.message_article_url_not_found),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .map { it.error }
                    .distinctUntilChanged()
                    .collect { error ->
                        error?.let {
                            Snackbar.make(
                                binding.root,
                                error,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun setNews(news: News) {
        // Set news info
        binding.apply {
            title.text = news.title
            Glide.with(this.root)
                .load(news.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(image)
            author.text = news.author
            date.text = news.publishedAt.formatDateToDaysAgo()
            content.text = news.content
        }

        // Check news bookmarks
        binding.btnBookmark.isChecked = viewModel.isArticleBookmarked(news)
    }

    companion object {
        const val EXTRA_NEWS = "extra_news"
    }
}