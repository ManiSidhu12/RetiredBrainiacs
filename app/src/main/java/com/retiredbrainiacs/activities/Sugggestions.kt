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
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.suggestions_screen.*
import java.io.StringReader

class Sugggestions : AppCompatActivity(){
    lateinit var v : View
    lateinit var type : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.suggestions_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView

        if(intent.extras != null && intent.extras.getString("type") != null){
         type =    intent.extras.getString("type")
            v.titletxt.text = type

            txt_main.text = type
        }
if(type.equals("Suggestions")){
    edt_suggestion.hint = "Please Enter Suggestions"
}
        else{
    edt_suggestion.hint = "Please Enter Message"

}

        work()
    }
    fun work(){
        btn_submit_suggestion.setOnClickListener {
if(validate()){
    if(CommonUtils.getConnectivityStatusString(this).equals("true")){
sendSuggestions()
    }
    else{
        CommonUtils.openInternetDialog(this)
    }
}
        }
    }
    fun validate (): Boolean{
        if(edt_name_suggest.text.toString().isEmpty()){
            Common.showToast(this,"Please Enter Name...")
            return false
        }
        else  if(edt_email_suggest.text.toString().trim().isEmpty()){
            Common.showToast(this,"Please Enter Email...")
            return false
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_suggest.text.toString().trim()).matches())
        {
            Common.showToast(this,"Please Enter Valid Email...")
            return false
        }
        else  if(edt_mobile_suggest.text.toString().isEmpty()){
            Common.showToast(this,"Please Enter Phone no ...")
            return false
        }
        else  if(edt_suggestion.text.toString().isEmpty()){
            if(type.equals("Suggestions")) {
                Common.showToast(this, "Please Enter Suggestion ...")
            }
            else{
                Common.showToast(this, "Please Enter Message ...")

            }
            return false
        }
        else{
            return true
        }
    }


  private fun sendSuggestions(){
    var url = GlobalConstants.API_URL+"contact_form"
    val pd = ProgressDialog.show(this, "", "Loading", false)

    val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
        pd.dismiss()
        val gson = Gson()
        val reader = JsonReader(StringReader(response))
        reader.isLenient = true
    var    root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
        if(root1.status.equals("true")) {
          Common.showToast(this,root1.message)
edt_name_suggest.text = Editable.Factory.getInstance().newEditable("")
edt_email_suggest.text = Editable.Factory.getInstance().newEditable("")
edt_mobile_suggest.text = Editable.Factory.getInstance().newEditable("")
edt_suggestion.text = Editable.Factory.getInstance().newEditable("")


        } else{
            Common.showToast(this,root1.message)

        }
    },

            com.android.volley.Response.ErrorListener { pd.dismiss() }) {
        @Throws(AuthFailureError::class)
        override fun getParams(): Map<String, String> {
            val map = HashMap<String, String>()
          //  map["user_id"]= SharedPrefManager.getInstance(this@Sugggestions).userId
            map["name"]= edt_name_suggest.text.toString()
            map["email"]= edt_email_suggest.text.toString().trim()
            map["mobile"]= edt_mobile_suggest.text.toString().trim()
            map["message"]= edt_suggestion.text.toString()
            if(type.equals("Suggestions")) {

                map["contact_type"] = "1"
            }
            else{
                map["contact_type"] = "0"
            }
            Log.e("map suggestion",map.toString())
            return map
        }
    }

    postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    val requestQueue = Volley.newRequestQueue(this)
    requestQueue.add(postRequest)

}

}