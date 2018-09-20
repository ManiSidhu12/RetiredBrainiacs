package com.retiredbrainiacs.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.login.LoginManager
import com.facebook.share.Share
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.SharedPrefManager
import com.squareup.picasso.Picasso
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

        txt_name_account.text = SharedPrefManager.getInstance(this@Account).name


        if (SharedPrefManager.getInstance(this@Account).userImg != null && !SharedPrefManager.getInstance(this@Account).userImg.isEmpty())
        {
         Picasso.with(this@Account).load(SharedPrefManager.getInstance(this@Account).userImg).into(img_user_account)
        }
        else{
           img_user_account.setImageResource(R.drawable.dummyuser)
        }

        if (SharedPrefManager.getInstance(this@Account).rating != null){
            rate_bar_account.rating = SharedPrefManager.getInstance(this@Account).rating.toFloat()
        }
            work()
    }

    fun work(){
           lay_logout.setOnClickListener{
            SharedPrefManager.getInstance(this@Account).logout()
            LoginManager.getInstance().logOut()
        }

        btn_edit.setOnClickListener {
startActivity(Intent(this@Account,ContactInfo::class.java).putExtra("type","edit"))
        }
        lay_chng_pswd.setOnClickListener {
            startActivity(Intent(this@Account,ChangePassword::class.java))
        }
        lay_about.setOnClickListener {
            startActivity(Intent(this@Account,AboutUs::class.java))
        }
        lay_suggestion.setOnClickListener {
            startActivity(Intent(this@Account,Sugggestions::class.java))

        }
    }
}