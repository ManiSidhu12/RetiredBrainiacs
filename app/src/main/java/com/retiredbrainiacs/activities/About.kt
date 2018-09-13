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
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.memorial.MemoHomeRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import java.io.StringReader

class About : AppCompatActivity(){
    lateinit var v : View
    var page_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.about)

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

                if(root.memorial[0].userData[0].image != null && !root.memorial[0].userData[0].image.isEmpty()){
                    Picasso.with(this).load(root.memorial[0].userData[0].image).into(img_memo_home)
                }
                else{

                }
                txtName.text = "About "+ root.memorial[0].userData[0].personName
              abouttxt.text = Editable.Factory.getInstance().newEditable(root.memorial[0].userData[0].sampleContent1)

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

                map["user_id"] = SharedPrefManager.getInstance(this@About).userId
                map["linkname"] = "about"

                Log.e("map get memo",map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}