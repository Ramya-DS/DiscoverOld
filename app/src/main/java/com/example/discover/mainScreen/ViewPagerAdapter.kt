package com.example.discover.mainScreen

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.discover.mainScreen.MainTabFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> MainTabFragment.newInstance(true)
        1 -> MainTabFragment.newInstance(false)
        else -> MainTabFragment.newInstance(false)
    }
}