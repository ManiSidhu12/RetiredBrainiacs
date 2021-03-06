package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
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
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.forgot_password.*
import kotlinx.android.synthetic.main.login_screen.*
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.util.*
import java.util.regex.Pattern

class Login : Activity(){

    lateinit var rootLogin : LoginRoot
    lateinit var callbackManager : CallbackManager
    lateinit var fb_name : String
    lateinit var fb_img : String
    lateinit var fb_email : String
    var authClient : TwitterAuthClient ?= null

    private val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {

                        requestData()
                    }

                    override fun onCancel() {
                        Common.showToast(this@Login,"Login Cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        Common.showToast(this@Login,exception.toString())
                    }
                })

        authClient = TwitterAuthClient()
       // window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.login_screen)

        Common.setFontRegular(this@Login,txt_logo_login)
        Common.setFontRegular(this@Login,txt_forgot)
        Common.setFontRegular(this@Login,txt_connect)
        Common.setFontRegular(this@Login,txt_new)
        Common.setFontRegular(this@Login,txt_signup)

        Common.setFontEditRegular(this@Login,edt_email_login)
        Common.setFontEditRegular(this@Login,edt_pswd_login)

        Common.setFontBtnRegular(this@Login,btn_login)
        login_button.callback = object : Callback<TwitterSession>() {
            override fun  success(result : Result<TwitterSession>) {
                val twitterSession : TwitterSession  = result.data
                authClient!!.requestEmail(twitterSession,object :  com.twitter.sdk.android.core.Callback<String>() {
                    override fun success(result1: Result<String>) {
                        Log.e("twitter result",result1.data.toString())

                    }

                    override fun failure(exception: TwitterException?) {
                    }

                })
            }

            override  fun failure( exception : TwitterException) {
            }
        }
        work()
    }

    fun work(){
        lay_google.setOnClickListener {
            login_button.performClick()
        }
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
txt_forgot.setOnClickListener {
    openDialog()
}
        btn_login.setOnClickListener {
            if (Common.validate(this@Login,edt_email_login,input_lay_email) && Common.validatePassword(this@Login,edt_pswd_login,input_lay_pswd)) {
                if (CommonUtils.getConnectivityStatusString(this@Login).equals("true")) {

                 loginWebService(edt_email_login.text.toString().trim(),edt_pswd_login.text.toString().trim())

                } else {
                    CommonUtils.openInternetDialog(this@Login)
                }
            }
        }

        lay_fb.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@Login, Arrays.asList("public_profile", "email"))

        }
       /* var authClient = TwitterAuthClient()
        var session = TwitterSession()
        authClient.requestEmail(session, object : Callback<String>() {
            override fun success(result: Result<String>) {
                // Do something with the result, which provides the email address
            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
            }
        })*/
    }

    fun requestData() {
        val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            val json = response.jsonObject
            Log.e("json",response.toString())
            if (json != null) {
                if (!json.has("email")) {
                    Common.showToast(this@Login,"Unable to get your email..")
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
        Log.e("data",data.toString())
        authClient!!.onActivityResult(requestCode, resultCode, data)
    }
    private fun checkUserWebService(email : String,pswd : String) {
        val url = GlobalConstants.API_URL + "check_email"
        val progressDialog = CommonUtils.createProgressDialog(this@Login)

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

                    if(CommonUtils.getConnectivityStatusString(this@Login).equals("true")){
                        loginWebService(email,pswd)
                    }
                    else{
                        CommonUtils.openInternetDialog(this@Login)


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

                    Common.showToast(this@Login, error.message.toString())

                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["email"] = email.trim()
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    //============= Login Web Service =====
    private fun loginWebService(email : String,pswd : String){
        val url = GlobalConstants.API_URL+"login"
        val pd = ProgressDialog.show(this@Login, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                Common.showToast(this@Login,"Logged In Successfully...")
                SharedPrefManager.getInstance(this@Login).verifyStatus = "true"

                SharedPrefManager.getInstance(this@Login).userLogin(rootLogin.data.userId,rootLogin.data.displayName,email,"",rootLogin.data.image,pswd,"")
                SharedPrefManager.getInstance(this@Login).rating = rootLogin.data.ratingUser
                val intent = Intent(this@Login, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else{
                Common.showToast(this@Login,rootLogin.message)
if(rootLogin.message.equals("YOUR ACCOUNT IS NOT VERIFIED")){
    val intent = Intent(this@Login, Verification::class.java)
    startActivity(intent)

}
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

    private fun openDialog() {
        val dialog = Dialog(this@Login, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.forgot_password)
        dialog.show()
        Common.setFontEditRegular(this@Login, dialog.edt_email_forgot)
        Common.setFontBtnRegular(this@Login, dialog.btn_ok)
        Common.setFontBtnRegular(this@Login, dialog.btn_cancel)
        Common.setFontRegular(this@Login, dialog.txt_title)
        Common.setFontRegular(this@Login, dialog.txt_enter)


        dialog.btn_ok.setOnClickListener{
            if (dialog.edt_email_forgot.text.isEmpty()) {
                Common.showToast(this@Login, "Please Enter Email")
            } else if (!EMAIL_ADDRESS_PATTERN.matcher(dialog.edt_email_forgot.text.toString()).matches()) {
                Common.showToast(this@Login, "Please Enter Valid Email")
            } else {
                dialog.dismiss()

                if(CommonUtils.getConnectivityStatusString(this@Login).equals("true")){
                   forgotPassword(dialog.edt_email_forgot.text.toString().trim(),dialog)
                }
                else{
                    CommonUtils.openInternetDialog(this@Login)
                }
            }
        }
        dialog.btn_cancel.setOnClickListener{ dialog.dismiss() }
    }
    private fun forgotPassword(email : String,dialog: Dialog){
        val url = GlobalConstants.API_URL+"forget_password"
        val pd = ProgressDialog.show(this@Login, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(dialog.edt_email_forgot.windowToken, 0)
                dialog.dismiss()
            } else{
                Common.showToast(this@Login,rootLogin.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["user_email"] = email
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}