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
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.memorial.MemoHomeRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.donations_screen.*
import kotlinx.android.synthetic.main.stories.*
import java.io.StringReader

class Donations : AppCompatActivity(){
  lateinit  var v : View
    var page_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donations_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Contact"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.VISIBLE

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
            getMemorial()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }
        v.btn_edit.setOnClickListener {
            if(v.btn_edit.text.equals("Edit")){
                v.btn_edit.text = "Save"
                edt_donate.isEnabled = true
                edt_charity.isEnabled = true
                edt_adrs.isEnabled = true
                edt_paypal.isEnabled = true

            }
            else{
                if(CommonUtils.getConnectivityStatusString(this).equals("true")){
                    addMemorialPage()                }
                else{
                    CommonUtils.openInternetDialog(this)
                }
            }
        }

    }
    private fun getMemorial() {
        var url = GlobalConstants.API_URL1+"?action=view_memorial_page"
        val pd = ProgressDialog.show(this,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var   root = gson.fromJson<MemoHomeRoot>(reader, MemoHomeRoot::class.java)
            if(root.status.equals("true")){

                page_id = root.memorial[0].userData[0].pageId

                if(root.memorial[0].candleLight[0].candle != null && !root.memorial[0].candleLight[0].candle.isEmpty()){
                    Picasso.with(this).load(root.memorial[0].candleLight[0].candle).into(img_donate)
                }
                else{

                }
                edt_donate.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent1)
                edt_charity.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].charities)
                edt_adrs.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].cheques)
                edt_paypal.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].paypal)

            }
            else{
                Common.showToast(this,root.message)
            }
        },

                Response.ErrorListener {
                    Log.e("error","err")
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Donations).userId
                map["linkname"] = "donations"

                Log.e("map get memo",map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    private fun addMemorialPage() {
        var url = GlobalConstants.API_URL + "add_memorial_page"
        val pd = ProgressDialog.show(this, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, com.android.volley.Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            Log.e("msg", root.status + root.message)
            if (root.status.equals("true")) {
                Common.showToast(this, root.message)
                v.btn_edit.text = "Save"
                edt_donate.isEnabled = false
                edt_paypal.isEnabled = false
                edt_adrs.isEnabled = false
                edt_charity.isEnabled = false


            } else {
                Common.showToast(this, root.message)

            }
        },

                com.android.volley.Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = java.util.HashMap<String, String>()
                map["user_id"] = SharedPrefManager.getInstance(this@Donations).userId
                map["act"] = "donations"
                map["person_name"] = ""
                map["date_of_birth"] = ""
                map["end_date"] = ""
                map["sample_content1"] = edt_donate.text.toString()
                map["sample_content2"] = edt_charity.text.toString()
                map["sample_content3"] = edt_adrs.text.toString()
                map["sample_content4"] = edt_paypal.text.toString()
                map["cover_photo"] = ""
                Log.e("map add memorial pages", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}