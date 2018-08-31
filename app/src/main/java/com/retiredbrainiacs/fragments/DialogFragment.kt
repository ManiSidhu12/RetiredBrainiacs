package com.retiredbrainiacs.fragments

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.retiredbrainiacs.R


 class DialogFragment : DialogFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.more_pop_up, container, false)
        dialog.setTitle("Simple Dialog")
        return rootView
    }
}