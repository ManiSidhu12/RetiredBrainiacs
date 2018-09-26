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
import com.google.gson.stream.JsonReader
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.FriendsAdapter
import com.retiredbrainiacs.apis.ApiClient
import com.retiredbrainiacs.apis.ApiInterface
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.EventListener
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.friend.AllFriendRoot
import com.retiredbrainiacs.model.friend.ListAll
import kotlinx.android.synthetic.main.all_classified_screen.view.*
import retrofit2.Retrofit
import java.io.StringReader
import java.util.*
import kotlin.collections.ArrayList

class AllFriendsFragment : Fragment(),EventListener{
    override fun sendDataToActivity(data: String, pos: Int) {
        Log.e("data",data)
        filter(data!!)
    }

    lateinit var v : View
    //============== Retrofit =========
    lateinit var retroFit: Retrofit
    lateinit var service: ApiInterface
    lateinit var gson: Gson
    lateinit var root : AllFriendRoot
    lateinit var adap : FriendsAdapter
    //==============
    var listFriends : MutableList<ListAll> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.all_classified_screen,container,false)

        v.recycler_stores.layoutManager = LinearLayoutManager(activity)


        // ============ Retrofit ===========
        service = ApiClient.getClient().create(ApiInterface::class.java)
        retroFit = ApiClient.getClient()
        gson = Gson()
        //====================

        if(CommonUtils.getConnectivityStatusString(activity).equals("true")){
            getAllUsersAPI()
        }
        else{
            CommonUtils.openInternetDialog(activity)
        }

        return v
    }

    //======= Get all Users API ====
    fun getAllUsersAPI() {

        var url = GlobalConstants.API_URL+"show_all_users"
        val pd = ProgressDialog.show(activity, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            v.recycler_stores.visibility = View.VISIBLE
Log.e("response",response)
            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<AllFriendRoot>(reader, AllFriendRoot::class.java)

            if (root != null) {
                if (root.status.equals("true")) {
                    v.recycler_stores.layoutManager = LinearLayoutManager(activity!!)
                    if(root.listAll != null && root.listAll.size > 0) {
                     adap = FriendsAdapter(activity!!, root.listAll, service, retroFit, gson)
                        v.recycler_stores.adapter = adap
                    }
                } else {
                    Common.showToast(activity!!, "No User Found...")
                    //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                }
            }
        },

                Response.ErrorListener { pd.dismiss()
                    v.recycler_stores.visibility = View.VISIBLE}) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(activity).userId
                Log.e("map all users", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(postRequest)
    }
    //******* Implementing filter method to search text in list
    fun filter(text:String) {
        // text = text.toLowerCase(Locale.getDefault())
        listFriends = ArrayList()
        if (text.length == 0)
        {
            listFriends = ArrayList()
            listFriends.addAll(root.listAll)
        }
        else
        {
            listFriends = ArrayList()
            for (i in 0 until root.listAll.size)
            {
                if (root.listAll[i].displayName.toLowerCase(Locale.getDefault()).contains(text))
                {
                    var m : ListAll
                    m = root.listAll[i]
                    listFriends.add(m)
                }
            }
        }

        adap = FriendsAdapter(activity!!, listFriends, service, retroFit, gson)
        v.recycler_stores.adapter = adap

    }
}