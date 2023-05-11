package com.andresuryana.schotersnews.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.andresuryana.schotersnews.data.model.News
import com.andresuryana.schotersnews.data.repository.NewsRepository
import com.andresuryana.schotersnews.util.NewsCategory
import com.andresuryana.schotersnews.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    data class HomeUiState(
        var isLoading: Boolean = false,
        var selectedCategory: NewsCategory = NewsCategory.values().first(),
        var error: String? = null
    )

    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var _headlines = MutableLiveData<List<News>>()
    val headlines: LiveData<List<News>> = _headlines

    private var _explore = MutableLiveData<List<News>>()
    val explore: LiveData<List<News>> = _explore

    init {
        loadArticles()
    }

    fun loadArticles() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _headlines.postValue(getTopHeadlines())
            _explore.postValue(getExplore(uiState.value.selectedCategory))
            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun getTopHeadlines(): List<News> = runBlocking(Dispatchers.IO) {
        when (val result = repository.getHeadlines()) {
            is Resource.Success -> result.data.articles
            is Resource.Failure -> {
                _uiState.update {
                    it.copy(
                        error = result.message
                    )
                }
                emptyList()
            }

            is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        error = result.message
                    )
                }
                emptyList()
            }
        }
    }

    private fun getExplore(category: NewsCategory): List<News> = runBlocking(Dispatchers.IO) {
        when (val result = repository.getExplore(category)) {
            is Resource.Success -> result.data.articles
            is Resource.Failure -> {
                _uiState.update {
                    it.copy(
                        error = result.message
                    )
                }
                emptyList()
            }

            is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        error = result.message
                    )
                }
                emptyList()
            }
        }
    }

    fun setCategory(category: NewsCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            _explore.postValue(getExplore(category))
            _uiState.update {
                it.copy(
                    selectedCategory = category
                )
            }
        }
    }
}