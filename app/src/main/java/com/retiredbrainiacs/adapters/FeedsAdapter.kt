package com.retiredbrainiacs.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.CommentListing
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.common.CommonUtils
import com.retiredbrainiacs.common.GlobalConstants
import com.retiredbrainiacs.common.SharedPrefManager
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_global.*
import kotlinx.android.synthetic.main.home_feed_adapter.view.*
import java.io.StringReader

class FeedsAdapter(var ctx: Context, var posts : MutableList<Post>,var type : String): RecyclerView.Adapter<FeedsAdapter.ViewHolder>(){
    val privacyArray = arrayOf("Public","Private")
    val actionsArray = arrayOf("Edit","Delete")

    lateinit var root: ResponseRoot

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.home_feed_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtUserName)
        Common.setFontRegular(ctx,holder.txtTime)
        Common.setFontRegular(ctx,holder.txtRating)
        Common.setFontRegular(ctx,holder.txtPost)
        Common.setFontRegular(ctx,holder.txtLike)
        Common.setFontRegular(ctx,holder.txtComment)
        Common.setFontBtnRegular(ctx,holder.btnPost)
        Common.setFontRegular(ctx,holder.edtCmnt)
       // Log.e("content",posts[position].postContent)
        holder.txtPost.text = posts[position].postContent

        holder.txtUserName.text = posts[position].wallPostUserName
        holder.txtLikeCount.text = posts[position].likeCount
        holder.txtCmntCount.text = posts[position].commentCount
        holder.txtTime.text = posts[position].postingDate
   if(posts[position].wallPostUserImage != null && !posts[position].wallPostUserImage.isEmpty()){
    Picasso.with(ctx).load(posts[position].wallPostUserImage).into(holder.imgUser)
   }
  else{
    holder.imgUser.setImageResource(R.drawable.dummyuser)
        }
        if(posts[position].image != null && !posts[position].image.isEmpty()){
            holder.imgPost.visibility = View.VISIBLE
            Picasso.with(ctx).load(posts[position].image).into(holder.imgPost)
        }
        else{
            holder.imgPost.visibility = View.GONE
        }

        if(posts[position].likedByMe.equals("1")){
            holder.txtLike.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
            holder.txtLikeCount.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
            holder.imgLike.setColorFilter(ContextCompat.getColor(ctx, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)

        }
        else{
            holder.txtLike.setTextColor(Color.parseColor("#90949C"))
            holder.txtLikeCount.setTextColor(Color.parseColor("#90949C"))
            holder.imgLike.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        holder.layCmnt.setOnClickListener {
            Log.e("data",posts[position].commentList.toString())
           CommentListing.getData(posts[position].commentList)
          ctx.startActivity(Intent(ctx,CommentListing::class.java).putExtra("id",posts[position].usersWallPostId))
            (ctx as AppCompatActivity).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up )
        }
        holder.edtCmnt.setOnClickListener {
            CommentListing.getData(posts[position].commentList)

           ctx.startActivity(Intent(ctx,CommentListing::class.java).putExtra("id",posts[position].usersWallPostId))
            (ctx as AppCompatActivity).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up )
        }

        if(type.equals("forum")){
            holder.laySettings.visibility = View.GONE
            holder.layLikes.visibility = View.GONE
            holder.layCmnts.visibility = View.GONE

        }
        else{
            holder.laySettings.visibility = View.VISIBLE
            holder.layLikes.visibility = View.VISIBLE
            holder.layCmnts.visibility = View.VISIBLE
            val adapterPrivacy = ArrayAdapter(ctx, R.layout.spin_setting1,privacyArray)
            adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
            holder.spinPrivacy.adapter = adapterPrivacy

            val adapterActions = ArrayAdapter(ctx, R.layout.spin_setting1,actionsArray)
            adapterActions.setDropDownViewResource(R.layout.spinner_txt)
            holder.spinActions.adapter = adapterActions
        }
     holder.spinActions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(holder.spinActions.selectedItem.equals("Delete")){
        showDialogMsg(ctx,posts[position].usersWallPostId,holder.layoutFeed)
        }
    }


}
holder.layLike.setOnClickListener {
    if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
        like(ctx,posts[position].usersWallPostId,holder.txtLike,holder.imgLike,holder.txtLikeCount)
    }
    else{
        CommonUtils.openInternetDialog(ctx)
    }
}
    }

    fun showDialogMsg(c: Context, id: String, layoutFeed: LinearLayout) {
        val globalDialog = Dialog(c, R.style.Theme_Dialog)
        globalDialog.setContentView(R.layout.dialog_global)
        globalDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)



        globalDialog.text.text = "Are you sure you want to delete?"



        val lp = WindowManager.LayoutParams()
        lp.copyFrom(globalDialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        globalDialog.show()
        globalDialog.window!!.attributes = lp
        globalDialog.ok.text = "Yes"
        globalDialog.cancel.text = "No"

        globalDialog.cancel.setOnClickListener{

            globalDialog.dismiss()


        }


        globalDialog.ok.setOnClickListener {
globalDialog.dismiss()
            if(CommonUtils.getConnectivityStatusString(ctx).equals("true")){
                deletePost(c,id,layoutFeed)
            }
        }


    }

    private fun like(ctx: Context, id: String, txtLike: TextView, imgLike: ImageView, txtLikeCount: TextView){
        var url = GlobalConstants.API_URL1+"?action=submit_like"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(ctx,root.message)
                txtLike.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
                txtLikeCount.setTextColor(ContextCompat.getColor(ctx, R.color.theme_color_orange))
               imgLike.setColorFilter(ContextCompat.getColor(ctx, R.color.theme_color_orange), android.graphics.PorterDuff.Mode.SRC_IN)
txtLikeCount.text = root.likeCount
            }
            else{
                Common.showToast(ctx,root.message)
                txtLike.setTextColor(Color.parseColor("#90949C"))
                txtLikeCount.setTextColor(Color.parseColor("#90949C"))
                imgLike.setColorFilter(Color.parseColor("#90949C"), android.graphics.PorterDuff.Mode.SRC_IN)
                txtLikeCount.text = root.likeCount

            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["post_id"] = id
                Log.e("map like",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    private fun deletePost(ctx: Context, id: String, layoutFeed: LinearLayout){
        var url = GlobalConstants.API_URL1+"?action=delete_post"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
          var  root1 = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root1.status.equals("true")){
                Common.showToast(ctx,root1.message)
                layoutFeed.visibility = View.GONE

            }
            else{
                Common.showToast(ctx,root1.message)

            }
        },

                Response.ErrorListener {
                    pd.dismiss()
                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()

                map["user_id"] = SharedPrefManager.getInstance(ctx).userId
                map["post_id"] = id
                Log.e("map like",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }


    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val imgUser = itemView.img_user_feed
        val txtUserName = itemView.txt_name_user
        val txtTime = itemView.txt_time
        val rateBar = itemView.rate_bar
        val txtRating = itemView.txt_rating
        val spinPrivacy = itemView.spin_privacy
        val txtPost = itemView.txt_post
        val imgPost = itemView.img_post
        val layLike = itemView.lay_like
        val txtLike = itemView.txt_like
        val txtComment = itemView.txt_cmnt
        val layCmnt = itemView.lay_cmnt
        val edtCmnt = itemView.edt_cmnt
        val btnPost = itemView.btn_post
        val laySettings = itemView.lay_settings
        val layLikes = itemView.lay_likes
        val layCmnts = itemView.lay_cmnts
        val txtLikeCount = itemView.txt_like_count
        val txtCmntCount = itemView.txt_cmnt_count
        val imgLike = itemView.img_like
        val imgCmnt = itemView.img_cmnt
        val spinActions = itemView.spin_actions
        val layoutFeed = itemView.layoutfeed
    }
}