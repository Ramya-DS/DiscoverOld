package com.example.discover.show

import com.example.discover.dataModel.credits.Cast
import com.example.discover.dataModel.credits.Crew

interface OnCreditSelectedListener {
    fun onCrewSelected(crew: List<Crew>)
    fun onCastSelected(cast: List<Cast>)
}