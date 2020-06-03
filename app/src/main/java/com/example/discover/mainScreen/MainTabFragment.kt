package com.example.discover.mainScreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import com.example.discover.section.SectionAdapter
import com.example.discover.section.SectionListViewModel
import java.lang.ref.WeakReference

class MainTabFragment : Fragment() {

    companion object {
        fun newInstance(isMovie: Boolean): MainTabFragment {
            val fragment = MainTabFragment()
            val bundle = Bundle()
            bundle.putBoolean("isMovie", isMovie)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isMovie = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isMovie = arguments?.getBoolean("isMovie")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_tab, container, false)

        val modelClass = SectionListViewModel::class.java

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        ).get(modelClass)

        val sectionRecyclerView = rootView.findViewById<RecyclerView>(R.id.sectionList)
        sectionRecyclerView.layoutManager = LinearLayoutManager(context)
        sectionRecyclerView.setHasFixedSize(true)
        sectionRecyclerView.adapter =
            SectionAdapter(
                isMovie,
                viewModel,
                WeakReference(activity!! as Activity)
            )

        return rootView
    }



}
