package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.SharedPrefManager
import kotlinx.android.synthetic.main.account_screen.view.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*

class  Account : Fragment(){
    lateinit var v : View
    lateinit var v1 : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      v = inflater.inflate(R.layout.account_screen,container,false)
        (activity as AppCompatActivity).supportActionBar!!.show()
        v1 = (activity as AppCompatActivity).supportActionBar!!.customView

        v1.btn_logout.visibility = View.GONE
        v1.titletxt.text = "Account"
        work()
        return v
    }
    fun work(){
        v.lay_logout.setOnClickListener {
            SharedPrefManager.getInstance(activity).logout()
            LoginManager.getInstance().logOut()
        }
    }
}