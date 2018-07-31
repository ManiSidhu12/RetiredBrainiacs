package com.retiredbrainiacs.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.retiredbrainiacs.model.classified.DetailsRoot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.classified_detail_fragment.view.*
import java.io.StringReader

class DetailFragment : Fragment(){
    lateinit var v : View
    lateinit var root : DetailsRoot
    var linkName : String =""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
v = inflater.inflate(R.layout.classified_detail_fragment,container,false)


        if(arguments!!.getString("linkname") != null) {
        linkName = arguments!!.getString("linkname")

        }
        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getClassifiedDetails()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }
        return v
    }

    private fun getClassifiedDetails(){
        var url = GlobalConstants.API_URL+"classified_detail"
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<DetailsRoot>(reader, DetailsRoot::class.java)

            if(root.status.equals("true")){

                v.ad_id_details.text = "Ad Id :-"+root.clssified[0].adId
                v.postad_time.text = root.clssified[0].postedOn
                v.description_details.text = root.clssified[0].description

                if(arguments!!.getString("img") != null && !arguments!!.getString("img").isEmpty()){
                    Picasso.with(activity).load(arguments!!.getString("img")).into(v.img_classified)
                }
                v.postad.text = arguments!!.getString("title")
            }
            else{
                Common.showToast(activity!!,root.message)
            }
        },

                Response.ErrorListener {
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                map["linkname"] = linkName
                Log.e("map details",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }

}