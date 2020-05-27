package com.example.discover.discover

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.discover.R
import com.example.discover.utils.LoadBackdropImage
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.ref.WeakReference


class DiscoverActivity : AppCompatActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disover)

        val toolbar: MaterialToolbar = findViewById(R.id.discover_toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.outlineProvider = null
        }

        setSupportActionBar(toolbar)

//        val movieCard: ImageView = findViewById(R.id.movieBackdrop)
//        val showsCard: ImageView = findViewById(R.id.showsBackdrop)


//        movieCard.setOnClickListener {
//
//        }



        val contentLayout = findViewById<LinearLayout>(R.id.contentLayout)

        sheetBehavior = BottomSheetBehavior.from(contentLayout)
        sheetBehavior.isFitToContents = false
        sheetBehavior.isHideable = false
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    fun toggleFilters() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        else
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.discover_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                toggleFilters()
                return true
            }
            R.id.reset -> {
                //reset
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

}
