package com.example.discover

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.discover.discover.DiscoverActivity
import com.example.discover.mainScreen.ViewPagerAdapter
import com.example.discover.search.SearchActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        val viewPager = findViewById<ViewPager2>(R.id.tabViewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = ViewPagerAdapter(
            supportFragmentManager,
            lifecycle
        )
        tabLayout.tabMode = TabLayout.MODE_FIXED
        viewPager.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.icon = when (position) {
                0 -> ContextCompat.getDrawable(this, R.drawable.ic_movie)
                1 -> ContextCompat.getDrawable(this, R.drawable.ic_tv_show)
                else -> ContextCompat.getDrawable(this, R.drawable.ic_tv_show)
            }
        }.attach()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.discover -> {
                Toast.makeText(this, "Discover", Toast.LENGTH_SHORT)
                startActivity(Intent(this, DiscoverActivity::class.java))
                true
            }
            R.id.search -> {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT)
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
