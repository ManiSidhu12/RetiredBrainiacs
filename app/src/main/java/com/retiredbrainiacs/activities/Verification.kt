package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.ProgressDialog
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
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import kotlinx.android.synthetic.main.otp_screen.*
import org.json.JSONObject

class Verification : Activity(){

    lateinit var sb : StringBuilder
    lateinit var sb1 : StringBuilder

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
                    verifyOTP()
                }
                else{
                    CommonUtils.openInternetDialog(this@Verification)
                }
            }
        }
    }

    private fun verifyOTP(){
        var url = GlobalConstants.API_URL+"verified"
        val pd = ProgressDialog.show(this@Verification, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
try{
    var obj : JSONObject = JSONObject(response)
    val status :  String = obj.getString("status")
    val msg : String = obj.getString("message")
    if(status.equals("true")) {
        Common.showToast(this@Verification,"Verified Successfully...")
        val intent = Intent(this@Verification, ContactInfo::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    } else{
        Common.showToast(this@Verification,msg)

    }
}
catch (exc : Exception){

}

        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["activation_key"] = sb1.toString()
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}