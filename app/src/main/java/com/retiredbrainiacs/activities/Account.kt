package com.retiredbrainiacs.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.SharedPrefManager
import kotlinx.android.synthetic.main.account_screen.*
import kotlinx.android.synthetic.main.account_screen.view.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*

class  Account : AppCompatActivity(){
    lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.account_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Account"
        work()
    }

    fun work(){
           lay_logout.setOnClickListener{
            SharedPrefManager.getInstance(this@Account).logout()
            LoginManager.getInstance().logOut()
        }
    }
}