package com.retiredbrainiacs.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import com.retiredbrainiacs.adapters.ChatListingAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.chat.ChatFirendsRoot
import com.retiredbrainiacs.model.friend.AllFriendRoot
import kotlinx.android.synthetic.main.all_classified_screen.view.*
import kotlinx.android.synthetic.main.chat_listing_screen.*
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.list_dialog.*
import java.io.StringReader

class ChatListing : AppCompatActivity(){
    lateinit var v: View
    var root1 : AllFriendRoot ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_listing_screen)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        v = supportActionBar!!.customView
        v.titletxt.text = "Recent Chat"
        v.btn_logout.visibility = View.VISIBLE
        v.btn_logout.setBackgroundResource(R.drawable.addicon)

        chat_list_friends.layoutManager = LinearLayoutManager(this)

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
getRecentChat()
            getAllFriendsAPI()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }

        v.btn_logout.setOnClickListener {
openDialog()
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
                    chat_list_friends.visibility = View.VISIBLE
                    no_recent_chat.visibility = View.GONE
                    //   recycler_cmnts_detail.adapter = CommentsAdapter(this@FeedDetails, root.commentList, edt_cmnt)
                    chat_list_friends.adapter = ChatListingAdapter(this,root.chatFriends)

                }
                else{
                    chat_list_friends.visibility = View.GONE
                    no_recent_chat.visibility = View.VISIBLE
                }

            }
            else{
                chat_list_friends.visibility = View.GONE
                no_recent_chat.visibility = View.VISIBLE
               // Common.showToast(this@ChatListing,root.message)

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
    private fun openDialog() {
        val dialog = Dialog(this@ChatListing, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.list_dialog)
        dialog.show()
        dialog.friends_recycler.layoutManager = LinearLayoutManager(this@ChatListing)
        if(root1 != null && root1!!.listFriends.size > 0){
            dialog.friends_recycler.adapter = Adapter(this@ChatListing, root1!!.listFriends)
        }
        dialog.close.setOnClickListener {
            dialog.dismiss()
        }
    }
    fun getAllFriendsAPI() {
        var url = GlobalConstants.API_URL+"show_all_friend"
        //val pd = ProgressDialog.show(this, "", "Loading", false)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
          //  pd.dismiss()
           // v.recycler_stores.visibility = View.VISIBLE

            val gson = Gson()
            val reader = JsonReader(StringReader(response))
            reader.isLenient = true
             root1 = gson.fromJson<AllFriendRoot>(reader, AllFriendRoot::class.java)

            if (root1 != null) {
                if (root1!!.status.equals("true")) {
               //     v.recycler_stores.layoutManager = LinearLayoutManager(this)
                    if(root1!!.listFriends!= null && root1!!.listFriends.size > 0) {
                        v.btn_logout.visibility = View.VISIBLE
                      //  v.recycler_stores.adapter = AdapterFriends(activity!!, root.listFriends, service, retroFit, gson)
                    }
                } else {
                    v.btn_logout.visibility = View.GONE
                  //  Common.showToast(this, "No Friend Found...")
                    //  v.recycler_feed.adapter = FeedsAdapter(activity!!,t.posts)

                }
            }
        },

                Response.ErrorListener {
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@ChatListing).userId
                Log.e("map all friend", map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)
    }

}