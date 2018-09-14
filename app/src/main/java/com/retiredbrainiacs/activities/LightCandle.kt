package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
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
import com.retiredbrainiacs.model.ResponseRoot
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.light_candle.*
import java.io.StringReader

class LightCandle : AppCompatActivity(){
    lateinit var v : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.light_candle)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)
        v = supportActionBar!!.customView
        v.titletxt.text = "Light a Candle"
        v.btn_logout.visibility = View.GONE
        v.btn_edit.visibility = View.GONE

        btn_save_wish.setOnClickListener {
            if(edt_wish.text.isEmpty()){
                Common.showToast(this,"Please type wish..")
            }
            else if(edt_wish.text.toString().length  > 25){
                Common.showToast(this,"Character length exceeded for wish..")

            }
            else{
if(CommonUtils.getConnectivityStatusString(this).equals("true")){

    lightCandle()
}
                else{
    CommonUtils.openInternetDialog(this)
                }
            }
        }
    }
    private fun lightCandle() {
        var url = GlobalConstants.API_URL1 + "?action=light_a_candle"
        val pd = ProgressDialog.show(this, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response", response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)
            if (root.status.equals("true")) {
                Common.showToast(this, root.message)

            } else {
                Common.showToast(this, root.message)
            }
        },

                Response.ErrorListener {
                    Log.e("error", "err")
                    pd.dismiss()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["page_user_id"] = SharedPrefManager.getInstance(this@LightCandle).userId
                map["message"] = edt_wish.text.toString()

                Log.e("map light candle", map.toString())
                return map
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}