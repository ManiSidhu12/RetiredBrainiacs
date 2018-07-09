package com.retiredbrainiacs.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.main_screen.*
import java.util.*


class MainScreen : Activity(){
    lateinit var callbackManager : CallbackManager
    lateinit var fb_name : String
    lateinit var fb_img : String
    lateinit var fb_email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {

                        requestData()
                    }

                    override fun onCancel() {
                        Common.showToast(this@MainScreen,"Login Cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        Common.showToast(this@MainScreen,exception.toString())
                    }
                })

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

        lay_fb_main.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@MainScreen, Arrays.asList("public_profile", "email"))

        }
    }
    fun requestData() {
        val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            val json = response.jsonObject
            if (json != null) {
                if (!json.has("email")) {
                    Common.showToast(this@MainScreen,"Unable to get your email..")
                }
                else {
                    fb_name = json.getString("name")
                    fb_email = json.getString("email")
                    fb_img = json.getJSONObject("picture").getJSONObject("data").getString("url")

                   /// checkUserWebService(fb_email,"$$"+ fb_email +"$$",fb_img,fb_name)
                    val intent = Intent(this@MainScreen, Home::class.java)
                    startActivity(intent)
                }


            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        Log.e("data",data.toString())

    }


}