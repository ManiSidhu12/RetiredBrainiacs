package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FeedsAdapter
import kotlinx.android.synthetic.main.home_feed_screen.view.*

class FeedsFragment : Fragment(){
    val privacyArray = arrayOf("Public","Private")

    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.home_feed_screen,container,false)


        v.recycler_feed.layoutManager = LinearLayoutManager(activity!!)
        v.recycler_feed.adapter = FeedsAdapter(activity!!)

        val adapterPrivacy = ArrayAdapter(activity, R.layout.spin_setting1,privacyArray)
        adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
        v.spin_privacy_feed.adapter = adapterPrivacy
        return v
    }
}