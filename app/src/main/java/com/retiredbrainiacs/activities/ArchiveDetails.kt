package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.archive.ArchiveDetailsRoot
import kotlinx.android.synthetic.main.archive_details.*
import java.io.StringReader

class ArchiveDetails : AppCompatActivity(){
    lateinit var root : ArchiveDetailsRoot
    var id : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.archive_details)

        if(intent != null && intent.extras != null && intent.extras.getString("id")!= null){
            id = intent.extras.getString("id")
        }

        recycler_archive_details.layoutManager = LinearLayoutManager(this@ArchiveDetails)

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
            getArchiveDetails()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }
    }

    private fun getArchiveDetails(){
        var url = GlobalConstants.API_URL+"view-archive-data"
        val pd = ProgressDialog.show(this@ArchiveDetails, "", "Loading", false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ArchiveDetailsRoot>(reader, ArchiveDetailsRoot::class.java)

            if(root.status.equals("true")) {
                if(root.listArch != null && root.listArch.size > 0){

                }

            } else{
                Common.showToast(this@ArchiveDetails,root.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@ArchiveDetails).userId
                map["id"] = id
                Log.e("map archive details",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this@ArchiveDetails)
        requestQueue.add(postRequest)

    }

}