package com.example.discover.movie


import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.discover.R
import com.example.discover.dataModel.credits.Cast
import com.example.discover.dataModel.credits.Crew
import com.example.discover.show.ShowActivity
import com.google.android.material.appbar.MaterialToolbar
import java.lang.ref.WeakReference
import java.util.ArrayList

class InfoDialogFragment : DialogFragment() {

    companion object {
        fun newInfoInstance(list: List<InfoClass>): InfoDialogFragment {
            val fragment = InfoDialogFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList("list", list as ArrayList<out Parcelable>)
            }
            return fragment
        }

        fun newCrewInstance(list: List<Crew>): InfoDialogFragment {
            val fragment = InfoDialogFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList("crew", list as ArrayList<out Parcelable>)
            }
            return fragment
        }

        fun newCastInstance(list: List<Cast>): InfoDialogFragment {
            val fragment = InfoDialogFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList("cast", list as ArrayList<out Parcelable>)
            }
            return fragment
        }
    }

    private var info: ArrayList<InfoClass>? = null
    private var crew: ArrayList<Crew>? = null
    private var cast: ArrayList<Cast>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            info = getParcelableArrayList<InfoClass>("list")
            crew = getParcelableArrayList<Crew>("crew")
            cast = getParcelableArrayList<Cast>("cast")

            Log.d("arguments", "$info $crew $cast")

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

        info?.let {
            if (activity is MovieActivity)
                recyclerView.adapter = InfoAdapter(it, activity as MovieActivity)
            else
                recyclerView.adapter = InfoAdapter(it, activity as ShowActivity)
        }

        cast?.let {
            Log.d("cast", it.toString())
            recyclerView.adapter = CreditAdapter(false, WeakReference(activity as Activity)).apply {
                this.castList= it
            }
            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            })
        }

        crew?.let {
            Log.d("crew", it.toString())
            recyclerView.adapter = CreditAdapter(true, WeakReference(activity as Activity)).apply {
                this.crewList = it
            }
            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            })
        }

        return rootView
    }


}
