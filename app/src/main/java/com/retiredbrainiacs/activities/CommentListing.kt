package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.adapters.CommentsAdapter
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.CommentList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cmnt_listing.*
import java.io.StringReader

class CommentListing : Activity(){
    lateinit var  commentList : MutableList<CommentList>
    var post_id = ""
lateinit var  adap : CommentsAdapter
    companion object {
        lateinit var  cmntList : MutableList<CommentList>

        fun getData(commentList: MutableList<CommentList>) {
            cmntList = commentList
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.cmnt_listing)

        if(SharedPrefManager.getInstance(this@CommentListing).userImg != null && !SharedPrefManager.getInstance(this@CommentListing).userImg.isEmpty()){
            Picasso.with(this@CommentListing).load(SharedPrefManager.getInstance(this@CommentListing).userImg).into(img)
        }
        else{
            img.setImageResource(R.drawable.dummyuser)
        }
if(intent != null && intent.extras != null && intent.extras.getString("id") != null){
    post_id = intent.extras.getString("id")
}
        commentList = CommentListing.cmntList
        if(commentList != null && commentList.size > 0){
            recycler_cmnts.layoutManager = LinearLayoutManager(this@CommentListing)
            var dividerItemDecoration = DividerItemDecoration(this@CommentListing, 1)
            recycler_cmnts.addItemDecoration(dividerItemDecoration)

             adap = CommentsAdapter(this@CommentListing,commentList,edt_cmnt,btn_post,post_id)
            recycler_cmnts.adapter = adap
        }

        btn_post.setOnClickListener {
            if(edt_cmnt.text.isEmpty()){
                Common.showToast(this@CommentListing,"Please Enter Comment...")
            }
            else{
                if(CommonUtils.getConnectivityStatusString(this@CommentListing).equals("true")){
                    if(!GlobalConstants.editStatus) {
                        comment()
                    }
                    else{
                       editComment()
                    }
                }
                else{
                    CommonUtils.openInternetDialog(this@CommentListing)
                }
            }
        }
    }
    private fun comment(){
        var url = GlobalConstants.API_URL1+"?action=post_comment"
        val pd = ProgressDialog.show(this@CommentListing,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
          var  root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root1.status.equals("true")){
                Common.showToast(this@CommentListing,root1.message)
                edt_cmnt.text = Editable.Factory.getInstance().newEditable("")

                if(root1.commentList != null && root1.commentList.size > 0){
                    commentList = root1.commentList
                    recycler_cmnts.layoutManager = LinearLayoutManager(this@CommentListing)
                    var dividerItemDecoration = DividerItemDecoration(this@CommentListing, 1)
                    recycler_cmnts.addItemDecoration(dividerItemDecoration)
                  adap = CommentsAdapter(this@CommentListing, commentList,edt_cmnt,btn_post,post_id)
                    recycler_cmnts.adapter = adap
                }

            }
            else{
                Common.showToast(this@CommentListing,root1.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@CommentListing).userId
                map["post_id"] = post_id
                map["comment"] = edt_cmnt.text.toString()
                Log.e("map cmnt",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

    private fun editComment(){
        var url = GlobalConstants.API_URL1+"?action=edit_comment"
        val pd = ProgressDialog.show(this@CommentListing,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var   root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(this@CommentListing,root.message)
           //     txtCmnt.text = edt_cmnt.text.toString()

                commentList[GlobalConstants.pos].comment = edt_cmnt.text.toString()
                edt_cmnt.text = Editable.Factory.getInstance().newEditable("")
                adap.notifyDataSetChanged()
               // recycler_cmnts.layoutManager = LinearLayoutManager(this@CommentListing)

              /*  var dividerItemDecoration = DividerItemDecoration(this@CommentListing, 1)
                recycler_cmnts.addItemDecoration(dividerItemDecoration)
                adap = CommentsAdapter(this@CommentListing,root1.commentList,edt_cmnt,btn_post,post_id)
                recycler_cmnts.adapter = adap*/

            }
            else{
                Common.showToast(this@CommentListing,root.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                // map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["post_id"] = post_id
                map["comment_id"] = GlobalConstants.cmntid
                map["comment"] = edt_cmnt.text.toString()
                Log.e("map edit comment",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }


}