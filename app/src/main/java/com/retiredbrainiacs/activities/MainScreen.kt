package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.login.LoginRoot
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.main_screen.*
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.util.*
import javax.security.auth.callback.Callback


class MainScreen : Activity(){
    lateinit var callbackManager : CallbackManager
    lateinit var fb_name : String
    lateinit var fb_img : String
    lateinit var fb_email : String
    lateinit var rootLogin : LoginRoot
    lateinit var authClient : TwitterAuthClient

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
         authClient = TwitterAuthClient()


//        authClient.requestEmail(session, object:Callback<String>() {
//            fun success(result:Result<String>) {
//                // Do something with the result, which provides the email address
//            }
//            fun failure(exception:TwitterException) {
//                // Do something on failure
//            }
//        })


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
        lay_twitter.setOnClickListener {
            login_button.performClick()
        }

        login_button.callback = object:com.twitter.sdk.android.core.Callback<TwitterSession>() {
            override  fun success(result: Result<TwitterSession>) {
                // Do something with result, which provides a TwitterSession for making API calls
val twitterSession : TwitterSession  = result.data
authClient.requestEmail(twitterSession,object :  com.twitter.sdk.android.core.Callback<String>() {
    override fun success(result1: Result<String>?) {
Log.e("twitter result",result1?.data)

    }

    override fun failure(exception: TwitterException?) {
    }

})
            }

            override   fun failure(exception: TwitterException) {
                // Do something on failure
                Log.e("exc",exception.message)
            }
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

                checkUserWebService(fb_email,"$$"+ fb_email +"$$")
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


        // Pass the activity result to the login button.
        login_button.onActivityResult(requestCode, resultCode, data);

        Log.e("data",data.toString())

    }
    private fun checkUserWebService(email : String,pswd : String) {
        val url = GlobalConstants.API_URL + "check_email"
       var progressDialog = CommonUtils.createProgressDialog(this@MainScreen)

        progressDialog!!.show()


        val postRequest = object : StringRequest(Request.Method.POST, url, { response ->
            if (progressDialog != null && progressDialog!!.isShowing()) {
                progressDialog!!.dismiss()
            }
            var obj: JSONObject? = null
            try {
                obj = JSONObject(response)
                val status = obj!!.getString("status")
                //  Log.e("res", response);
                if (status.equals("true")) {
                    val msg = obj!!.getString("message")
                    //  Common.showToast(this@Welcome, msg)

                    if(CommonUtils.getConnectivityStatusString(this@MainScreen).equals("true")){
                        loginWebService(email,pswd)
                    }
                    else{
                        CommonUtils.openInternetDialog(this@MainScreen)


                    }
                } else {

                    val msg = obj!!.getString("message")


                  //  startActivity(Intent(this@Login,SocialSignUp::class.java).putExtra("name",nm).putExtra("email",email).putExtra("pswd",pswd).putExtra("image",img))
                   // finish()
                    // Common.showToast(this@Login, msg)

                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
                { error: VolleyError ->
                    if (progressDialog != null && progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }

                    Common.showToast(this@MainScreen, error.message.toString())

                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = java.util.HashMap<String, String>()
                map["email"] = email.trim()
                //Log.e("map", map.toString());
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    //============= Login Web Service =====
    private fun loginWebService(email : String,pswd : String){
        var url = GlobalConstants.API_URL+"login"
        val pd = ProgressDialog.show(this@MainScreen, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                SharedPrefManager.getInstance(this@MainScreen).setVerifyStatus("true")

                Common.showToast(this@MainScreen,"Logged In Successfully...")
                SharedPrefManager.getInstance(this@MainScreen).userLogin(rootLogin.data.userId,rootLogin.data.displayName,email,"",rootLogin.data.image,pswd,"")
                val intent = Intent(this@MainScreen, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else{
                Common.showToast(this@MainScreen,rootLogin.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["email"] = email
                map["password"] = pswd
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }


}