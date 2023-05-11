package com.andresuryana.schotersnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andresuryana.schootersnews.R
import com.andresuryana.schootersnews.databinding.ActivityMainBinding
import com.andresuryana.schotersnews.data.adapter.MainPagerAdapter
import com.andresuryana.schotersnews.data.adapter.MainPagerAdapter.Companion.SCREEN_BOOKMARK
import com.andresuryana.schotersnews.data.adapter.MainPagerAdapter.Companion.SCREEN_HOME
import com.andresuryana.schotersnews.data.adapter.MainPagerAdapter.Companion.SCREEN_PROFILE
import com.andresuryana.schotersnews.data.adapter.MainPagerAdapter.Companion.SCREEN_SEARCH
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup navigation
        setupViewPager()
        setupBottomNavigation()
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = MainPagerAdapter(this@MainActivity)
            isUserInputEnabled = false
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener {
            binding.viewPager.currentItem = when (it.itemId) {
                R.id.homeFragment -> SCREEN_HOME
                R.id.searchFragment -> SCREEN_SEARCH
                R.id.bookmarkFragment -> SCREEN_BOOKMARK
                R.id.profileFragment -> SCREEN_PROFILE
                else -> SCREEN_HOME
            }
            true
        }
    }
}