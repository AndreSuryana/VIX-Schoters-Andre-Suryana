package com.andresuryana.schotersnews.ui.bookmark

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
class BookmarkViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    data class BookmarkUiState(
        var isLoading: Boolean = false,
        var error: String? = null
    )

    private var _uiState = MutableStateFlow(BookmarkUiState())
    val uiState = _uiState.asStateFlow()

    private var _bookmarkList = MutableLiveData<List<News>>()
    val bookmarkList: LiveData<List<News>> = _bookmarkList

    init {
        getBookmarkedArticles()
    }

    fun getBookmarkedArticles() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getBookmarkedNews()) {
                is Resource.Success -> {
                    _bookmarkList.postValue(result.data.toList())
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
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