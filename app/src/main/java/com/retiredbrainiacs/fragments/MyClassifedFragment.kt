package com.retiredbrainiacs.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
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
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.MyClassifiedAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.classified.MyClassifiedRoot
import kotlinx.android.synthetic.main.all_classified_screen.view.*
import java.io.StringReader

class MyClassifedFragment : Fragment(){
    lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.all_classified_screen,container,false)


        v.recycler_stores.layoutManager = LinearLayoutManager(activity)


        if(CommonUtils.getConnectivityStatusString(activity!!).equals("true")){
            getMyClassified()
        }
        else{
            CommonUtils.openInternetDialog(activity!!)
        }
        return v
    }

    private fun getMyClassified() {
        var url = GlobalConstants.API_URL1+"?action=my_classified"
        val pd = ProgressDialog.show(activity!!,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            Log.e("response",response)

            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var   root = gson.fromJson<MyClassifiedRoot>(reader, MyClassifiedRoot::class.java)
            if(root.status.equals("true")){
                if(root.myClassified != null && root.myClassified.size >0){
v.recycler_stores.adapter = MyClassifiedAdapter(activity!!,root.myClassified)

                }
                else{
                    v.recycler_stores.visibility = View.GONE
                }

            }
            else{
                Common.showToast(activity!!,root.message)
                v.recycler_stores.visibility = View.GONE


            }
        },

                Response.ErrorListener {
                    Log.e("error","err")
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity!!).userId

                Log.e("map get my classified",map.toString())
                return map
            }
        }




        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }

}