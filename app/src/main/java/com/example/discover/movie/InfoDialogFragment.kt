package com.example.discover.movie


import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.discover.R
import com.example.discover.dataModel.movieDetail.MovieDetails
import com.example.discover.dataModel.movieDetail.SpokenLanguages
import com.google.android.material.appbar.MaterialToolbar
import java.util.ArrayList

class InfoDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(list: List<InfoClass>): InfoDialogFragment {
            val fragment = InfoDialogFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList("list", list as ArrayList<out Parcelable>)
            }
            return fragment
        }
    }

    lateinit var list: ArrayList<InfoClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            list = getParcelableArrayList<InfoClass>("list")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_info_dialog, container, false)

        val toolbar: MaterialToolbar = rootView.findViewById(R.id.toolbar_info)
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.info_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = InfoAdapter(list)

        return rootView
    }


}
