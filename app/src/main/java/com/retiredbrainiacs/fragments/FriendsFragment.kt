package com.retiredbrainiacs.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.friends_screen.*
import kotlinx.android.synthetic.main.friends_screen.view.*

class FriendsFragment : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.friends_screen,container,false)

        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView
        if(v1.btn_logout.visibility == View.VISIBLE) {
            v1.btn_logout.visibility = View.GONE
        }
        v1.titletxt.text = "Friends"


        Common.setFontRegular(activity!!,v1.titletxt)

        //========
        Common.setFontRegular(activity!!,v.txt_all_friend)
        Common.setFontRegular(activity!!,v.txt_request)
        Common.setFontRegular(activity!!,v.txt_accept)
        Common.setFontEditRegular(activity!!,v.edt_srch_friends)
        //========

        showSelection(v.txt_all_friend,v.img_all_friend,v.txt_request,v.txt_accept,v.img_request,v.img_accept,v.lay_all_friend,v.lay_request_friend,v.lay_accept_friend)
        fragmentManager!!.beginTransaction().replace(R.id.frame_friends,AllFriendsFragment()).commit()


        work()
        return  v
    }


    fun work(){
        v.lay_all_friend.setOnClickListener {
            showSelection(v.txt_all_friend, v.img_all_friend, v.txt_request, v.txt_accept, v.img_request, v.img_accept, v.lay_all_friend, v.lay_request_friend, v.lay_accept_friend)
            fragmentManager!!.beginTransaction().replace(R.id.frame_friends,AllFriendsFragment()).commit()

        }
        v.lay_request_friend.setOnClickListener {
            showSelection(v.txt_request, v.img_request, v.txt_all_friend, v.txt_accept, v.img_all_friend, v.img_accept,  v.lay_request_friend, v.lay_all_friend,v.lay_accept_friend)
            fragmentManager!!.beginTransaction().replace(R.id.frame_friends,RequestsFragment()).commit()

        }
        v.lay_accept_friend.setOnClickListener {
            showSelection(v.txt_accept, v.img_accept, v.txt_all_friend, v.txt_request, v.img_all_friend, v.img_request, v.lay_accept_friend,v.lay_all_friend, v.lay_request_friend)
            fragmentManager!!.beginTransaction().replace(R.id.frame_friends,AllFriendsFragment()).commit()

        }
    }
    fun showSelection(txtSelected: TextView, imgSelected: ImageView, txt2: TextView, txt3: TextView, img2: ImageView, img3: ImageView, laySelected: RelativeLayout, lay1: RelativeLayout, lay2: RelativeLayout){
        txtSelected.setTextColor(resources.getColor(R.color.theme_color_orange))
        imgSelected.setColorFilter(ContextCompat.getColor(activity!!, R.color.theme_color_orange))
        laySelected.setBackgroundColor(Color.parseColor("#FFF5EF"))
        txt2.setTextColor(resources.getColor(R.color.black))
        txt3.setTextColor(resources.getColor(R.color.black))
        img2.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        img3.setColorFilter(ContextCompat.getColor(activity!!, R.color.black))
        lay1.setBackgroundColor(Color.parseColor("#FFFFFF"))
        lay2.setBackgroundColor(Color.parseColor("#FFFFFF"))

    }
}