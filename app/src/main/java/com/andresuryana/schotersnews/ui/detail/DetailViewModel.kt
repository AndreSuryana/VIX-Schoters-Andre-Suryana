package com.andresuryana.schotersnews.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.data.repository.NewsRepository
import com.andresuryana.schotersnews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    data class DetailUiState(
        var isBookmarked: Boolean = false,
        var error: String? = null
    )

    private var _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    fun isArticleBookmarked(news: News?): Boolean = runBlocking(Dispatchers.IO) {
        if (news != null) {
            when (val result = repository.isNewsBookmarked(news)) {
                is Resource.Success -> result.data
                else -> false
            }
        } else false
    }

    fun updateBookmarkArticle(news: News, bookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DetailViewModel", "updateBookmarkArticle: $bookmarked")
            val result = if (bookmarked) repository.bookmarkNews(news)
            else repository.removeBookmarkedNews(news)
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isBookmarked = bookmarked)
                    }
                }
                is Resource.Failure -> {
                    _uiState.update {
                        it.copy(error = result.message)
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(error = result.message)
                    }
                }
            }
        }
    }
}