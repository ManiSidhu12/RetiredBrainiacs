package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import kotlinx.android.synthetic.main.forum_adapter.view.*

class ForumAdapter(var ctx : Context) : RecyclerView.Adapter<ForumAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.forum_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser = itemView.img_user_forum
        val txtUserName = itemView.txt_name_forum
        val txtTime = itemView.txt_time_forum
        val rateBar = itemView.rate_bar_forum
        val txtRating = itemView.txt_rating_forum
        val txtPost = itemView.txt_post_forum
        val postData = itemView.txt_data_forum
        val layReply = itemView.lay_reply
        val layView = itemView.lay_view

    }
}