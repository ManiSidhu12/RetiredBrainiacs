package com.retiredbrainiacs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.main_screen.*


class MainScreen : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_screen)

Common.setFontRegular(this@MainScreen,txt_moto_main)
Common.setFontRegular(this@MainScreen,txt_join_main)
Common.setFontRegular(this@MainScreen,txt_terms_main)
Common.setFontRegular(this@MainScreen,txt_already_main)
Common.setFontRegular(this@MainScreen,txt_sign_in_main)
Common.setFontRegular(this@MainScreen,txt_or_main)
Common.setFontRegular(this@MainScreen,txt_signup_main)
        work()


    }

    fun work(){
        txt_sign_in_main.setOnClickListener {
            val intent = Intent(this@MainScreen, Login::class.java)
            startActivity(intent)
            //finish()
        }

        txt_signup_main.setOnClickListener {
            val intent = Intent(this@MainScreen, SignUp::class.java)
            startActivity(intent)
          //  finish()
        }
    }
}