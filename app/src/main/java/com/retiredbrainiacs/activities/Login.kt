package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.login.LoginRoot
import kotlinx.android.synthetic.main.forgot_password.*
import kotlinx.android.synthetic.main.login_screen.*
import java.io.StringReader
import java.util.regex.Pattern

class Login : Activity(){

    lateinit var rootLogin : LoginRoot

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
    }

    //============= Login Web Service =====
    private fun loginWebService(email : String,pswd : String){
        var url = GlobalConstants.API_URL+"login"
        val pd = ProgressDialog.show(this@Login, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                Common.showToast(this@Login,"Logged In Successfully...")
                SharedPrefManager.getInstance(this@Login).userLogin(rootLogin.id,rootLogin.name,email,"","",pswd)
                val intent = Intent(this@Login, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else{
                Common.showToast(this@Login,rootLogin.message)

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


        dialog.btn_ok.setOnClickListener(View.OnClickListener {
            if (dialog.edt_email_forgot.text.isEmpty()) {
                Common.showToast(this@Login, "Please Enter Email")
            } else if (!EMAIL_ADDRESS_PATTERN.matcher(dialog.edt_email_forgot.text.toString()).matches()) {
                Common.showToast(this@Login, "Please Enter Valid Email")
            } else {
                dialog.dismiss()

                if(CommonUtils.getConnectivityStatusString(this@Login).equals("true")){
                    //forgotPasswordWebService(dialog,dialog.edt_email_forgot)
                }
                else{
                    CommonUtils.openInternetDialog(this@Login)
                }
            }
        })
        dialog.btn_cancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
    }

}