package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ChatListingAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.chat.ChatFirendsRoot
import kotlinx.android.synthetic.main.chat_listing_screen.*
import java.io.StringReader

class ChatListing : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_listing_screen)

        chat_list_friends.layoutManager = LinearLayoutManager(this)

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
getRecentChat()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }
    }
    private fun getRecentChat(){
        var url = GlobalConstants.API_URL1+"?action=chat_list_friends"
        val pd = ProgressDialog.show(this@ChatListing,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
           var root = gson.fromJson<ChatFirendsRoot>(reader, ChatFirendsRoot::class.java)

            if(root.status.equals("true")){

                if(root.chatFriends != null && root.chatFriends.size > 0){
                    //   recycler_cmnts_detail.adapter = CommentsAdapter(this@FeedDetails, root.commentList, edt_cmnt)
                    chat_list_friends.adapter = ChatListingAdapter(this,root.chatFriends)
                }

            }
            else{
                Common.showToast(this@ChatListing,root.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@ChatListing).userId
                Log.e("map chat list",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}