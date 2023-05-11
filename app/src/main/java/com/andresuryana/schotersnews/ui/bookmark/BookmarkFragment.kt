package com.andresuryana.schotersnews.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.FragmentBookmarkBinding
import com.andresuryana.schotersnews.data.adapter.NewsAdapter
import com.andresuryana.schotersnews.ui.detail.DetailActivity
import com.andresuryana.schotersnews.util.BaseFragment
import com.andresuryana.schotersnews.util.SnackbarHelper.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : BaseFragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<BookmarkViewModel>()

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        _binding = FragmentBookmarkBinding.inflate(layoutInflater)

        // Set title
        binding?.appBar?.title?.text = getString(R.string.title_bookmark)

        // Observe ui state
        observeUiState()
        observeBookmarkList()

        // Setup recycler view
        setupRecyclerView()

        // Setup swipe refresh layout
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.getBookmarkedArticles()
            binding?.swipeRefresh?.isRefreshing = false
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.getBookmarkedArticles()
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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    private fun observeBookmarkList() {
        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            newsAdapter.setList(it)
        }
    }
}