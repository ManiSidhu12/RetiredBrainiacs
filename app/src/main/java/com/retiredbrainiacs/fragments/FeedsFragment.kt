package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.facebook.login.LoginManager
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FeedsAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.SharedPrefManager
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.home_feed_screen.view.*

class FeedsFragment : Fragment(){
    val privacyArray = arrayOf("Public","Private")

    lateinit var v : View
    lateinit var v1 : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.home_feed_screen,container,false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView

        v1.btn_logout.visibility = View.GONE
        v1.titletxt.text = "Home"

        Common.setFontRegular(activity!!,v1.titletxt)


        v.recycler_feed.layoutManager = LinearLayoutManager(activity!!)
        v.recycler_feed.adapter = FeedsAdapter(activity!!)


        //======= Font =========
        Common.setFontRegular(activity!!,v.status)
        Common.setFontRegular(activity!!,v.audio)
        Common.setFontRegular(activity!!,v.image)
        Common.setFontEditRegular(activity!!,v.edt_srch)
        Common.setFontEditRegular(activity!!,v.edt_post_data)
        Common.setFontBtnRegular(activity!!,v.btn_post_feed)
        //=======================

        val adapterPrivacy = ArrayAdapter(activity, R.layout.spin_setting1,privacyArray)
        adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
        v.spin_privacy_feed.adapter = adapterPrivacy
        return v



    }
}