package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.View
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.advertising_screen.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.StringReader

class Advertising : AppCompatActivity(){
    lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.advertising_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView

        v.titletxt.text = "Advertisers"

        btn_submit_advertise.setOnClickListener {
if(validate()){
    if(CommonUtils.getConnectivityStatusString(this).equals("true")){
advertise()
    }
    else{
        CommonUtils.openInternetDialog(this)
    }
}
        }

    }
    fun validate() : Boolean{
        if(edt_cmpny_name.text.toString().isEmpty()){
            Common.showToast(this,"Please Enter Company Name...")
            return false
        }
        else  if(edt_website_url.text.toString().isEmpty()){
            Common.showToast(this,"Please Enter Website URL...")
            return false
        }
        else  if(edt_fname.text.toString().isEmpty()){
            Common.showToast(this,"Please Enter Name...")
            return false
        }
        else  if(edt_position.text.toString().isEmpty()){
            Common.showToast(this,"Please Enter Position...")
            return false
        }
        else  if(edt_email_adrs.text.toString().trim().isEmpty()){
            Common.showToast(this,"Please Enter Email...")
            return false
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_adrs.text.toString().trim()).matches())
        {
            Common.showToast(this,"Please Enter Valid Email...")
            return false
        }
        else{
            return true
        }
    }


    private fun advertise(){
        val url = GlobalConstants.API_URL+"advertising_request"
        val pd =  ProgressDialog.show(this, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            val  root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            if(root1.status.equals("true")) {
                Common.showToast(this,root1.message)
edt_cmpny_name.text = Editable.Factory.getInstance().newEditable("")
edt_website_url.text = Editable.Factory.getInstance().newEditable("")
edt_email_adrs.text = Editable.Factory.getInstance().newEditable("")
edt_fname.text = Editable.Factory.getInstance().newEditable("")
edt_position.text = Editable.Factory.getInstance().newEditable("")


            } else{
                Common.showToast(this,root1.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
               // map["user_id"]= SharedPrefManager.getInstance(this@Sugggestions).userId
                map["company"]= edt_cmpny_name.text.toString()
                map["website_url"]= edt_website_url.text.toString().trim()
                map["email_address"]= edt_email_adrs.text.toString().trim()
                map["name"]= edt_fname.text.toString()
                map["position"]= edt_position.text.toString()

                Log.e("map advertise",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}