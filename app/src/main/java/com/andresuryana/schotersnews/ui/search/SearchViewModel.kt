package com.andresuryana.schotersnews.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    data class SearchUiState(
        var isLoading: Boolean = false,
        var error: String? = null
    )

    private var _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var _listNews = MutableLiveData<List<News>>()
    val listNews: LiveData<List<News>> = _listNews

    fun searchArticles(keyword: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!keyword.isNullOrBlank()) {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                when (val result = repository.searchNews(keyword)) {
                    is Resource.Success -> {
                        _listNews.postValue(result.data.articles)
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}