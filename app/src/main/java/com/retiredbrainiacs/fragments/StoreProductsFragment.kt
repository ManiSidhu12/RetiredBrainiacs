package com.retiredbrainiacs.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.StoreProductAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.model.store.StoreProductsRoot
import kotlinx.android.synthetic.main.all_classified_screen.view.*
import java.io.StringReader

class StoreProductsFragment : Fragment(){
    lateinit var v : View
    lateinit var productRoot : StoreProductsRoot
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.all_classified_screen,container,false)

        v.recycler_stores.layoutManager = LinearLayoutManager(activity)


        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getProductsWebService()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }
        return v
    }


    //======= Get Products API ======
    private fun getProductsWebService(){
        var url = GlobalConstants.API_URL+"list_all_products"
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            productRoot = gson.fromJson<StoreProductsRoot>(reader, StoreProductsRoot::class.java)

            if(productRoot.status.equals("true")) {

                if(productRoot.listProducts != null && productRoot.listProducts.size > 0){
                    v.recycler_stores.adapter = StoreProductAdapter(activity!!,productRoot.listProducts)
                }

            } else{
            //    Common.showToast(activity!!,productRoot.message)

            }
        },

                Response.ErrorListener { pd.dismiss() }) {

        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)

    }

}