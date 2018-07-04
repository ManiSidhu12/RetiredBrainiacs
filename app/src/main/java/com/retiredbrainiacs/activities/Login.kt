package com.retiredbrainiacs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import kotlinx.android.synthetic.main.login_screen.*

class Login : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        Common.setFontRegular(this@Login,txt_logo_login)
        Common.setFontRegular(this@Login,txt_forgot)
        Common.setFontRegular(this@Login,txt_connect)
        Common.setFontRegular(this@Login,txt_new)
        Common.setFontRegular(this@Login,txt_signup)

        Common.setFontEditRegular(this@Login,edt_email_login)
        Common.setFontEditRegular(this@Login,edt_pswd_login)

        Common.setFontBtnRegular(this@Login,btn_login)

        work()
    }

    fun work(){
        txt_signup.setOnClickListener {
            val intent = Intent(this@Login, SignUp::class.java)
            startActivity(intent)
        }
        edt_email_login.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Common.validate(this@Login, edt_email_login, input_lay_email)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        edt_pswd_login.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Common.validatePassword(this@Login, edt_pswd_login, input_lay_pswd)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        btn_login.setOnClickListener {
            if (Common.validate(this@Login,edt_email_login,input_lay_email) && Common.validatePassword(this@Login,edt_pswd_login,input_lay_pswd)) {
                if (CommonUtils.getConnectivityStatusString(this@Login).equals("true")) {

                 //   loginWebService(edt_email_login.text.toString().trim(),edt_pswd_login.text.toString().trim())

                } else {
                    CommonUtils.openInternetDialog(this@Login)
                }
            }
        }
    }


}