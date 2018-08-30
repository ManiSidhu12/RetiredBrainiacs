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
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.ChatAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.chat.ChatRoot
import kotlinx.android.synthetic.main.chat_screen.*
import java.io.StringReader
import android.app.Activity
import android.text.Editable
import android.view.inputmethod.InputMethodManager


class Chat : AppCompatActivity(){
    lateinit var root : ChatRoot
    var linkname = ""
    var to_id = ""
    lateinit var adap : ChatAdapter
    var pd : ProgressDialog ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.chat_screen)
        recycler_chat.layoutManager = LinearLayoutManager(this)
        if(intent.extras != null && intent.extras.getString("linkname") != null){
            linkname = intent.extras.getString("linkname")
            to_id = intent.extras.getString("toId")
        }

        if(CommonUtils.getConnectivityStatusString(this).equals("true")){
             pd = ProgressDialog.show(this@Chat,"","Loading",false)

            getChat()
        }
        else{
            CommonUtils.openInternetDialog(this)
        }
        sendbtn.setOnClickListener {
            if(txttype.text.isEmpty()){
                Common.showToast(this,"Please type message...")
            }
            else {
                if (CommonUtils.getConnectivityStatusString(this).equals("true")) {
                    val imm : InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(txttype.windowToken, 0)
                    sendMessage()
                } else {
                    CommonUtils.openInternetDialog(this)
                }
            }
        }
    }
    private fun getChat(){
        var url = GlobalConstants.API_URL1+"?action=chat_list"
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            if(pd != null) {
                pd!!.dismiss()
            }
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
             root = gson.fromJson<ChatRoot>(reader, ChatRoot::class.java)

            if(root.status.equals("true")){

                if(root.chatList != null && root.chatList.size > 0){
                    //   recycler_cmnts_detail.adapter = CommentsAdapter(this@FeedDetails, root.commentList, edt_cmnt)
                 adap = ChatAdapter(this,root.chatList)
                 recycler_chat.adapter = adap
                }

            }
            else{
                Common.showToast(this@Chat,root.message)


            }
        },

                Response.ErrorListener {
                    if(pd != null) {
                        pd!!.dismiss()
                    }
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Chat).userId
                map["linkname"] = linkname
                Log.e("map get chat",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }
    private fun sendMessage(){
        var url = GlobalConstants.API_URL1+"?action=message_to_friend"
        val pd = ProgressDialog.show(this@Chat,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
         var   root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                txttype.text = Editable.Factory.getInstance().newEditable("")
                Common.showToast(this@Chat,"Message Sent...")
getChat()

            }
            else{
                Common.showToast(this@Chat,root.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@Chat).userId
                map["to_user_id"] = to_id
                map["post_content"] = txttype.text.toString()
                map["message_img"] = ""
                Log.e("map get chat",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    override fun onResume() {
        super.onResume()
    }
}