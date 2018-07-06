package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ForumAdapter
import kotlinx.android.synthetic.main.forum_screen.view.*

class ForumFragment : Fragment(){
    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = layoutInflater.inflate(R.layout.forum_screen,container,false)
        v.recycler_forum.layoutManager = LinearLayoutManager(activity)
        v.recycler_forum.adapter = ForumAdapter(activity!!)
        return v
    }
}