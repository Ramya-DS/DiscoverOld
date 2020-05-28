package com.example.discover.search


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discover.R
import java.lang.ref.WeakReference

class SearchResultFragment : Fragment() {
    companion object {

        fun newInstance(type: String): SearchResultFragment {
            val fragment = SearchResultFragment()
            fragment.arguments = Bundle().apply {
                putString("type", type)
            }

            return fragment
        }
    }

    lateinit var adapter: MediaLinearAdapter
    lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString("type")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_search_result, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.searchResult)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        adapter = MediaLinearAdapter(type, WeakReference(activity as Activity))
        recyclerView.adapter = adapter


        var loading = true
        var pastVisibleItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            Log.v("...", "Last Item Wow !")
                            (activity as SearchActivity).fetchMore()
                        }
                    }
                }
            }
        })
        return rootView
    }


}
