package com.retiredbrainiacs.activities

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
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
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.fragments.FeedsFragment
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.CommentList
import com.retiredbrainiacs.model.feeds.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cmnt_listing.*
import java.io.StringReader

class CommentListing : Activity(),EventListener{
    override fun sendDataToActivity(data: String?, pos: Int) {

    }

    lateinit var  commentList : MutableList<CommentList>
    var post_id = ""
    lateinit var  adap : CommentsAdapter
    lateinit var listner : EventListener
    var pos : Int = 0
    lateinit var post : Post


    companion object {
        lateinit var  cmntList : MutableList<CommentList>
        lateinit var posts : MutableList<Post>
        var positionPost : Int = 0
        fun getData(post: MutableList<Post>, position: Int) {
            cmntList = post[position].commentList
            posts = post
            positionPost = position
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

            listner = this

if(intent != null && intent.extras != null && intent.extras.getString("id") != null){
    post_id = intent.extras.getString("id")
    pos = intent.extras.getInt("position")
}

        commentList = CommentListing.cmntList
            post = CommentListing.posts[positionPost]
            txt_like_count_detail.text = post.likeCount
            txt_cmnt_count_detail.text = post.commentCount
            Log.e("count",post.likeCount)
            if(post.likedByMe.equals("1")){
                txt_like_count_detail.text = post.likeCount

                txt_like_count_detail.setTextColor(ContextCompat.getColor(this, R.color.theme_color_orange))
                txt_like_detail.setTextColor(ContextCompat.getColor(this, R.color.theme_color_orange))
                img_like_detail.setColorFilter(ContextCompat.getColor(this, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)

            }
            else{
                txt_like_count_detail.setTextColor(Color.parseColor("#90949C"))
                txt_like_detail.setTextColor(Color.parseColor("#90949C"))
                img_like_detail.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)

            }
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

            lay_like_detail.setOnClickListener {
                if(CommonUtils.getConnectivityStatusString(this@CommentListing).equals("true")){
                    like()
                }
                else{
                    CommonUtils.openInternetDialog(this@CommentListing)
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

                // CommentListing.posts[CommentListing.positionPost].commentList = root1.commentList
                  GlobalConstants.count = root1.commentList.size.toString()
                    listner.sendDataToActivity(commentList.size.toString(),pos)
                    post.commentCount = root1.commentList.size.toString()
                    post.commentList = root1.commentList

                    txt_cmnt_count_detail.text = post.commentCount

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
    private fun like(){
        var url = GlobalConstants.API_URL1+"?action=submit_like"
        val pd = ProgressDialog.show(this@CommentListing,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
       var   root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(this@CommentListing,root.message)
                if(root.message.equals("like")) {
                    txt_like_count_detail.setTextColor(ContextCompat.getColor(this@CommentListing, R.color.theme_color_orange))
                    txt_like_detail.setTextColor(ContextCompat.getColor(this@CommentListing, R.color.theme_color_orange))
                    img_like_detail.setColorFilter(ContextCompat.getColor(this@CommentListing, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)
post.likedByMe = "1"
                }
                else{
                    txt_like_count_detail.setTextColor(Color.parseColor("#90949C"))
                    txt_like_detail.setTextColor(Color.parseColor("#90949C"))
                    img_like_detail.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)
                    post.likedByMe = "0"

                }
                txt_like_count_detail.text = root.likeCount
                post.likeCount = root.likeCount

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

                map["user_id"] = SharedPrefManager.getInstance(this@CommentListing).userId
                map["post_id"] = post_id
                Log.e("map like",map.toString())
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

             //   listner.sendDataToActivity(commentList.size.toString(),pos)
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