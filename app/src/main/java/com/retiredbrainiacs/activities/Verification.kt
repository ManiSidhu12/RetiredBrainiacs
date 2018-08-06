package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.retiredbrainiacs.common.GoEditTextListener
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.login.LoginRoot
import kotlinx.android.synthetic.main.otp_screen.*
import org.json.JSONObject
import java.io.StringReader

class Verification : Activity(){

    lateinit var sb : StringBuilder
    lateinit var sb1 : StringBuilder

    lateinit var rootLogin : LoginRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.otp_screen)

        Common.setFontRegular(this@Verification,code)
        Common.setFontEditRegular(this@Verification,edt1)
        Common.setFontEditRegular(this@Verification,edt2)
        Common.setFontEditRegular(this@Verification,edt3)
        Common.setFontEditRegular(this@Verification,edt4)
        Common.setFontBtnRegular(this@Verification,btn_verify)
        Common.setFontBtnRegular(this@Verification,btn_resend)

        sb = StringBuilder()



        work()

    }

    fun work(){
         edt1.addListener(object : GoEditTextListener{
             override fun onUpdate(){
                 val clipMan = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                 // otpView.setText(clipMan.getText());

                 if (clipMan.text.toString() != null && clipMan.text.length == 4) {
                     Log.e("text",clipMan.text.substring(0,1))
                     edt1.setText(clipMan.text.substring(0,1))
                     edt2.setText(clipMan.text.substring(1,2))
                     edt3.setText(clipMan.text.substring(2,3))
                     edt4.setText(clipMan.text.substring(3,4))
                 }
             }
            })
        edt2.addListener(object : GoEditTextListener{
            override fun onUpdate(){
                val clipMan = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // otpView.setText(clipMan.getText());

                if (clipMan.text.toString() != null && clipMan.text.length == 4) {
                    Log.e("text",clipMan.text.substring(0,1).toString())
                    edt1.setText(clipMan.text.substring(0,1))
                    edt2.setText(clipMan.text.substring(1,2))
                    edt3.setText(clipMan.text.substring(2,3))
                    edt4.setText(clipMan.text.substring(3,4))
                }
            }
        })
        edt3.addListener(object : GoEditTextListener{
            override fun onUpdate(){
                val clipMan = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // otpView.setText(clipMan.getText());

                if (clipMan.text.toString() != null && clipMan.text.length == 4) {
                    Log.e("text",clipMan.text.substring(0,1).toString())
                    edt1.setText(clipMan.text.substring(0,1))
                    edt2.setText(clipMan.text.substring(1,2))
                    edt3.setText(clipMan.text.substring(2,3))
                    edt4.setText(clipMan.text.substring(3,4))
                }
            }
        })
        edt4.addListener(object : GoEditTextListener{
            override fun onUpdate(){
                val clipMan = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // otpView.setText(clipMan.getText());

                if (clipMan.text.toString() != null && clipMan.text.length == 4) {
                    Log.e("text",clipMan.text.substring(0,1).toString())
                    edt1.setText(clipMan.text.substring(0,1))
                    edt2.setText(clipMan.text.substring(1,2))
                    edt3.setText(clipMan.text.substring(2,3))
                    edt4.setText(clipMan.text.substring(3,4))
                }
            }
        });
        edt1.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(sb.length == 0){
                    edt1.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(sb.length == 1){
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (sb.length == 0  && edt1.text.toString().length == 1) {
                    sb.append(s)
                    // edt1.clearFocus();
                    edt2.requestFocus()
                    edt2.isCursorVisible = true

                }
            }

        })

        edt2.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (sb.length == 0) {

                    edt2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (sb.length == 1) {

                    sb.deleteCharAt(0)

                }            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (sb.length == 0 && edt2.text.toString().length == 1) {
                    sb.append(s)
                    edt2.clearFocus()
                    edt3.requestFocus()
                    edt3.isCursorVisible = true

                }         }

        })

        edt3.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (sb.length == 0 && edt3.text.toString().length == 1) {
                    sb.append(s)
                    edt3.clearFocus()
                    edt4.requestFocus()
                    edt4.isCursorVisible = true

                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (sb.length == 1) {

                    sb.deleteCharAt(0)

                }            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (sb.length == 0) {

                    edt3.requestFocus()
                }
            }

        })
        edt4.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        btn_verify.setOnClickListener {
            sb1 = StringBuilder()
            sb1.append(edt1.text.toString().trim())
            sb1.append(edt2.text.toString().trim())
            sb1.append(edt3.text.toString().trim())
            sb1.append(edt4.text.toString().trim())
            Log.e("string",sb1.toString())
            if(sb1.length < 4){
                Log.e("no","valid")
                Common.showToast(this@Verification,"Please fill valid OTP..")
            }
            else{
                if(CommonUtils.getConnectivityStatusString(this@Verification).equals("true")){
                   // verifyOTP()
                    signUpWebService()
                }
                else{
                    CommonUtils.openInternetDialog(this@Verification)
                }
            }
        }
        btn_resend.setOnClickListener {
            if(CommonUtils.getConnectivityStatusString(this@Verification).equals("true")){
                sendActivationKeyWebService()
            }
            else{
                CommonUtils.openInternetDialog(this@Verification)
            }
        }
    }

    private fun sendActivationKeyWebService(){
        var url = GlobalConstants.API_URL+"send_activation_key"
        val pd = ProgressDialog.show(this@Verification, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {

            } else{
                Common.showToast(this@Verification,rootLogin.message)
            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_email"] = SharedPrefManager.getInstance(this@Verification).userEmail
                map["username"] = SharedPrefManager.getInstance(this@Verification).name
                map["password"] = SharedPrefManager.getInstance(this@Verification).password
                Log.e("map key",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    //============== Sign Up Web Service =====
    private fun signUpWebService(){
        var url = GlobalConstants.API_URL+"sign_up_from"
        val pd = ProgressDialog.show(this@Verification, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            rootLogin = gson.fromJson<LoginRoot>(reader, LoginRoot::class.java)

            if(rootLogin.status.equals("true")) {
                Common.showToast(this@Verification,"Registered Successfully...")
                SharedPrefManager.getInstance(this@Verification).setUserID(rootLogin.userId)
                val intent = Intent(this@Verification, ContactInfo::class.java)
                intent.putExtra("type","verify")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else{
                Common.showToast(this@Verification,rootLogin.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["first_name"] = SharedPrefManager.getInstance(this@Verification).name
                map["last_name"] = ""
                map["email"] = SharedPrefManager.getInstance(this@Verification).userEmail
                map["dob"] = SharedPrefManager.getInstance(this@Verification).dob
                map["password"] = SharedPrefManager.getInstance(this@Verification).password
                map["con_pswd"] = SharedPrefManager.getInstance(this@Verification).password
                if(SharedPrefManager.getInstance(this@Verification).gender.equals("Male")){
                    map["gender"] = "1"
                }
                else{
                    map["gender"] = "2"
                }
               // map["gender"] = SharedPrefManager.getInstance(this@Verification).gender
                if(SharedPrefManager.getInstance(this@Verification).maritalStatus.equals("Single")){
                    map["marital_status"] = "1"
                }
                else{
                    map["marital_status"] = "2"
                }
             //   map["marital_status"] = SharedPrefManager.getInstance(this@Verification).maritalStatus
                map["activation_key"] = sb1.toString()
                Log.e("map signup",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}