package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.MyClassifiedAdapter
import kotlinx.android.synthetic.main.all_classified_screen.view.*

class MyClassifedFragment : Fragment(){
    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.all_classified_screen,container,false)


        v.recycler_stores.layoutManager = LinearLayoutManager(activity)

        v.recycler_stores.adapter = MyClassifiedAdapter(activity!!)
        return v
    }
}