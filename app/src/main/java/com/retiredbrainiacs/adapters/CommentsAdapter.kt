package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.feeds.CommentList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comments_adapter.view.*

class CommentsAdapter(var ctx: Context, var commentList: MutableList<CommentList>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.comments_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
    return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.edtcmnt.text = Editable.Factory.getInstance().newEditable(commentList[position].comment)
        if(commentList[position].displayName != null && !commentList[position].displayName.isEmpty()){
            holder.txtName.text = commentList[position].displayName
        }
        if(commentList[position].wallPostImage != null && !commentList[position].wallPostImage.isEmpty()){
          Picasso.with(ctx).load(commentList[position].wallPostImage).into(holder.imgUser)
        }

    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val imgUser = itemView.img_user_cmnt
        val edtcmnt = itemView.edt_cmnt
        val txtName = itemView.txt_name_cmnt
        val txtTime = itemView.txt_time_cmnt
        val ratingBar = itemView.rate_bar_cmnt
    }
}