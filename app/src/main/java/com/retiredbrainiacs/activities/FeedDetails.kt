package com.retiredbrainiacs.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.View
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
import com.retiredbrainiacs.model.feeds.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_action_bar.view.*
import kotlinx.android.synthetic.main.feed_details_screen.*
import java.io.StringReader

class FeedDetails : AppCompatActivity(){
    lateinit var post : Post
    lateinit var root : ResponseRoot
    lateinit var  commentList : MutableList<CommentList>

    companion object {
    lateinit var  cmntList : MutableList<CommentList>

    fun getData(commentList: MutableList<CommentList>) {
         cmntList = commentList
    }
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.feed_details_screen)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        var v = supportActionBar!!.customView
        v.titletxt.text = "Details"
        lay_settings_detail.visibility = View.GONE
        if(intent != null && intent.extras.getParcelable<Post>("list") != null) {
         post = intent.extras.getParcelable("list")

            if(post.wallPostUserImage != null && !post.wallPostUserImage.isEmpty()){
                Picasso.with(this@FeedDetails).load(post.wallPostUserImage).into(img_user_feed_detail)
            }
            else{
                img_user_feed_detail.visibility = View.GONE
            }
            if(post.image != null && !post.image.isEmpty()){
                Picasso.with(this@FeedDetails).load(post.image).into(img_post_detail)
            }
            else{
                img_post_detail.setImageResource(R.drawable.no_image)
            }


            txt_post_detail.text = post.postContent

            txt_name_user_detail.text = post.wallPostUserName
            txt_like_count_detail.text = post.likeCount
            if(post.likedByMe.equals("1")){
             txt_like_detail.setTextColor(ContextCompat.getColor(this@FeedDetails, R.color.theme_color_orange))
                txt_like_count_detail.setTextColor(ContextCompat.getColor(this, R.color.theme_color_orange))
               img_like_detail.setColorFilter(ContextCompat.getColor(this, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)

            }
            else{
                txt_like_detail.setTextColor(Color.parseColor("#90949C"))
                txt_like_count_detail.setTextColor(Color.parseColor("#90949C"))
                img_like_detail.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)

            }
            txt_cmnt_count_detail.text = post.commentCount
            Log.e("true",post.toString())
commentList = FeedDetails.cmntList
            if(commentList != null && commentList.size > 0){
                recycler_cmnts_detail.layoutManager = LinearLayoutManager(this@FeedDetails)
                recycler_cmnts_detail.adapter = CommentsAdapter(this@FeedDetails,commentList)
            }
}
btn_post_detail.setOnClickListener {
    if(edt_cmnt_detail.text.isEmpty()){
        Common.showToast(this@FeedDetails,"Please Enter Comment...")
    }
    else{
        if(CommonUtils.getConnectivityStatusString(this@FeedDetails).equals("true")){
comment()
        }
        else{
            CommonUtils.openInternetDialog(this@FeedDetails)
        }
    }
}
    }

    private fun comment(){
        var url = GlobalConstants.API_URL1+"?action=post_comment"
        val pd = ProgressDialog.show(this@FeedDetails,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(this@FeedDetails,root.message)
                edt_cmnt_detail.text = Editable.Factory.getInstance().newEditable("")

                if(root.commentList != null && root.commentList.size > 0){
                    recycler_cmnts_detail.layoutManager = LinearLayoutManager(this@FeedDetails)
                    recycler_cmnts_detail.adapter = CommentsAdapter(this@FeedDetails,root.commentList)
                }

            }
            else{
                Common.showToast(this@FeedDetails,root.message)


            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(this@FeedDetails).userId
                map["post_id"] = post.usersWallPostId
                map["comment"] = edt_cmnt_detail.text.toString()
                Log.e("map like",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(postRequest)

    }

}