package com.example.wembleymoviesapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wembleymoviesapp.databinding.ActivityMainBinding
import com.example.wembleymoviesapp.ui.base.BaseBindingActivity
import com.example.wembleymoviesapp.ui.viewModel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseBindingActivity<ActivityMainBinding, MainViewModel>() {
    var tabsText = arrayListOf("Movies", "Favorites")

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun setupView() {
        bindData()
    }

    private fun bindData() = with(activityViewModel) {
        binding.apply {
            viewPager2.adapter = ViewPageAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager2) { tabItem, page ->
                tabItem.text = tabsText[page]
            }.attach()
        }
    }

    inner class ViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int { return 2 }

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> MoviesFragment()
                else -> FavoritesFragment()
            }
        }
    }
}