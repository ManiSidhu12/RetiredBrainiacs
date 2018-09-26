package com.retiredbrainiacs.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
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
import com.retiredbrainiacs.common.EventListener
import com.retiredbrainiacs.model.ResponseRoot
import com.retiredbrainiacs.model.feeds.CommentList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comments_adapter.view.*
import kotlinx.android.synthetic.main.dialog_global.*
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(var ctx: Context, var commentList: MutableList<CommentList>, var edt_cmnt: EditText, var btn_post: Button, var post_id: String,var txt_cmnt_count_detail: TextView) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){
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

        if(commentList[position].edit == 1){
holder.layEdit.visibility = View.VISIBLE
        }
        else{
            holder.layEdit.visibility = View.GONE
        }
        holder.txtTime.text = commentList[position].commentTime
    holder.btnEdit.setOnClickListener {
    GlobalConstants.editStatus = true
    GlobalConstants.cmntid = commentList[position].commentId
    GlobalConstants.pos = position
    edt_cmnt.text = Editable.Factory.getInstance().newEditable(commentList[position].comment)
edt_cmnt.setSelection(edt_cmnt.text.toString().length)
    }

        holder.btnDelete.setOnClickListener {
            GlobalConstants.editStatus = false
          showDialogMsg(ctx,post_id,commentList[position].commentId,holder.mainLayout,commentList,position,txt_cmnt_count_detail)
        }


    }
    private fun deleteComment(ctx: Context, post_id: String, cmnt_id: String, commentList: MutableList<CommentList>, position: Int,txt_count : TextView){
        var url = GlobalConstants.API_URL1+"?action=delete_comment"
        val pd = ProgressDialog.show(ctx,"","Loading",false)
        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            pd.dismiss()
            val gson = Gson()
            val reader = com.google.gson.stream.JsonReader(StringReader(response))
            reader.isLenient = true
            var root = gson.fromJson<ResponseRoot>(reader, ResponseRoot::class.java)

            if(root.status.equals("true")){
                Common.showToast(ctx,root.message)
                commentList.removeAt(position)

                    txt_count.text = commentList.size.toString()

                notifyDataSetChanged()
              //  mainLayout.visibility = View.GONE
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
                Log.e("map delete comment",map.toString())
                return map
            }
        }

        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(ctx)
        requestQueue.add(postRequest)

    }
    fun showDialogMsg(c: Context, post_id: String, commentId: String, mainLayout: RelativeLayout, commentList: MutableList<CommentList>, position: Int,txt_count :TextView) {
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
                deleteComment(ctx, post_id,commentList[position].commentId,commentList,position,txt_count)
            }
            else{
                CommonUtils.openInternetDialog(ctx)
            }
        }


    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val imgUser = itemView.img_user_cmnt
        val txtCmnt = itemView.txtcmnt
        val txtName = itemView.txt_name_cmnt
        val txtTime = itemView.txtTime
        val ratingBar = itemView.rate_bar_cmnt
        val btnEdit = itemView.edit_cmnt
        val btnDelete = itemView.delete_cmnt
        val mainLayout = itemView.cmntsLay
        val layEdit = itemView.layedit
    }
}