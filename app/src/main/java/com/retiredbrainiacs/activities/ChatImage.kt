package com.retiredbrainiacs.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.retiredbrainiacs.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.fullimage_sccreen.*

class ChatImage : AppCompatActivity(){
    lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fullimage_sccreen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Image"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.VISIBLE
        v.btn_edit.text = "Dismiss"
        btn_edit_img.visibility = View.GONE

        if(intent.extras.getString("img") != null){
            Picasso.with(this@ChatImage).load(intent.extras.getString("img")).into(image_zoom)
        }
        v.btn_edit.setOnClickListener {
            finish()
        }
    }

}