package com.andresuryana.schotersnews.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.FragmentSearchBinding
import com.andresuryana.schotersnews.data.adapter.NewsAdapter
import com.andresuryana.schotersnews.ui.detail.DetailActivity
import com.andresuryana.schotersnews.util.BaseFragment
import com.andresuryana.schotersnews.util.SnackbarHelper.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        _binding = FragmentSearchBinding.inflate(layoutInflater)

        // Set title
        binding?.appBar?.title?.text = getString(R.string.title_search)

        // Setup search bar
        setupSearchBar()

        // Observe ui state
        observeUiState()
        observeSearchResult()

        // Setup recycler view
        setupRecyclerView()

        // Setup swipe refresh layout
        binding?.swipeRefresh?.setOnRefreshListener {
            val keyword = binding?.searchBar?.editText?.text?.trim().toString()
            viewModel.searchArticles(keyword)
            binding?.swipeRefresh?.isRefreshing = false
        }

        return binding?.root
    }

    private fun setupSearchBar() {
        binding?.searchBar?.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.searchArticles(v.text.trim().toString())
                true
            } else false
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickListener { news ->
            Intent(activity, DetailActivity::class.java).also {
                it.putExtra(DetailActivity.EXTRA_NEWS, news)
                activity?.startActivity(it)
            }
        }
        binding?.rvResult?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    showLoadingDialog(state.isLoading)
                    state.error?.let { showErrorSnackbar(requireView(), it) }
                }
            }
        }
    }

    private fun observeSearchResult() {
        viewModel.listNews.observe(viewLifecycleOwner) {
            newsAdapter.setList(it)
        }
    }
}