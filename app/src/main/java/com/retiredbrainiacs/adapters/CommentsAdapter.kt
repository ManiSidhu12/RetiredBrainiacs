package com.retiredbrainiacs.adapters

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.*
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.CommentList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comments_adapter.view.*
import java.io.StringReader

class CommentsAdapter(var ctx: Context, var commentList: MutableList<CommentList>, var edt_cmnt: EditText,var btn_post: Button,var post_id : String) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){
    var listener: EventListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.comments_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
    return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCmnt.text = commentList[position].comment
        if(commentList[position].displayName != null && !commentList[position].displayName.isEmpty()){
            holder.txtName.text = commentList[position].displayName
        }
        if(commentList[position].wallPostImage != null && !commentList[position].wallPostImage.isEmpty()){
          Picasso.with(ctx).load(commentList[position].wallPostImage).into(holder.imgUser)
        }
holder.btnEdit.setOnClickListener {
    GlobalConstants.editStatus = true
    GlobalConstants.cmntid = commentList[position].commentId
    GlobalConstants.pos = position
    edt_cmnt.text = Editable.Factory.getInstance().newEditable(commentList[position].comment)
}

        holder.btnDelete.setOnClickListener {
            GlobalConstants.editStatus = false
        }


    }
    private fun editComment(ctx: Context, post_id: String,cmnt_id : String,edt : EditText,txtCmnt : TextView){
        var url = GlobalConstants.API_URL1+"?action=edit_comment"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
         var   root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(ctx,root.message)
                txtCmnt.text = edt.text.toString()
                edt.text = Editable.Factory.getInstance().newEditable("")

            }
            else{
                Common.showToast(ctx,root.message)


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
                map["comment_id"] = cmnt_id
                map["comment"] = edt.text.toString()
                Log.e("map edit comment",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val imgUser = itemView.img_user_cmnt
        val txtCmnt = itemView.txtcmnt
        val txtName = itemView.txt_name_cmnt
        val txtTime = itemView.txtTime
        val ratingBar = itemView.rate_bar_cmnt
        val btnEdit = itemView.edit_cmnt
        val btnDelete = itemView.delete_cmnt
    }
}