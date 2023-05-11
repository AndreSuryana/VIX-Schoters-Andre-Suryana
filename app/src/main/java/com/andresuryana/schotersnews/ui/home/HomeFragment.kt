package com.andresuryana.schotersnews.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.FragmentHomeBinding
import com.andresuryana.schotersnews.data.adapter.CategoryAdapter
import com.andresuryana.schotersnews.data.adapter.NewsAdapter
import com.andresuryana.schotersnews.data.adapter.HeadlineAdapter
import com.andresuryana.schotersnews.ui.detail.DetailActivity
import com.andresuryana.schotersnews.util.BaseFragment
import com.andresuryana.schotersnews.util.SnackbarHelper.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var headlineAdapter: HeadlineAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        // Set title
        binding?.appBar?.title?.text = getString(R.string.title_home)

        // Data observer
        observeUiState()
        observeHeadlines()
        observeExplore()

        // Layout setup
        setupHeadlineAdapter()
        setupCategoryAdapter()
        setupExploreAdapter()

        // Setup swipe refresh layout
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.loadArticles()
            binding?.swipeRefresh?.isRefreshing = false
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupHeadlineAdapter() {
        headlineAdapter = HeadlineAdapter()
        headlineAdapter.setOnItemClickListener { news ->
            Intent(activity, DetailActivity::class.java).also {
                it.putExtra(DetailActivity.EXTRA_NEWS, news)
                activity?.startActivity(it)
            }
        }
        binding?.rvHeadlines?.apply {
            adapter = headlineAdapter
            layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        }
    }

    private fun setupCategoryAdapter() {
        categoryAdapter = CategoryAdapter()
        categoryAdapter.setOnItemClickListener {
            viewModel.setCategory(it)
            categoryAdapter.setSelectedNewsCategory(it)
        }
        binding?.rvCategory?.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        }
    }

    private fun setupExploreAdapter() {
        newsAdapter = NewsAdapter()
        newsAdapter.setOnItemClickListener { news ->
            Intent(activity, DetailActivity::class.java).also {
                it.putExtra(DetailActivity.EXTRA_NEWS, news)
                activity?.startActivity(it)
            }
        }
        binding?.rvExplore?.apply {
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

    private fun observeHeadlines() {
        viewModel.headlines.observe(viewLifecycleOwner) {
            headlineAdapter.setList(it)
        }
    }

    private fun observeExplore() {
        viewModel.explore.observe(viewLifecycleOwner) {
            newsAdapter.setList(it)
        }
    }
}