package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.feeds.CommentList

class CommentsAdapter(var ctx: Context,var commentList: MutableList<CommentList>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.comments_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
    return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

    }
}