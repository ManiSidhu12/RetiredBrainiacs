package com.retiredbrainiacs.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.retiredbrainiacs.R
import kotlinx.android.synthetic.main.custom_action_bar.view.*

class Sugggestions : AppCompatActivity(){
    lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.suggestions_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Suggestions"
    }
}