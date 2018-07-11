package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ForumAdapter
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.forum_screen.view.*

class ForumFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = layoutInflater.inflate(R.layout.forum_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
        }
        v1.titletxt.text = "Forum"
        Common.setFontRegular(activity!!,v1.titletxt)
        Common.setFontEditRegular(activity!!,v.edt_srch_forum)

        v.recycler_forum.layoutManager = LinearLayoutManager(activity)
        v.recycler_forum.adapter = ForumAdapter(activity!!)
        return v
    }
}