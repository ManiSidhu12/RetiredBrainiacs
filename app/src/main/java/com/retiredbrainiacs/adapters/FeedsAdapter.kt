package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import com.retiredbrainiacs.model.feeds.Post
import kotlinx.android.synthetic.main.home_feed_adapter.view.*

class FeedsAdapter(var ctx: Context): RecyclerView.Adapter<FeedsAdapter.ViewHolder>(){
    val privacyArray = arrayOf("Public","Private")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.home_feed_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontBold(ctx,holder.txtUserName)
        Common.setFontRegular(ctx,holder.txtTime)
        Common.setFontRegular(ctx,holder.txtRating)
        Common.setFontRegular(ctx,holder.txtPost)
        Common.setFontRegular(ctx,holder.txtLike)
        Common.setFontRegular(ctx,holder.txtComment)
        Common.setFontBtnRegular(ctx,holder.btnPost)
        Common.setFontEditRegular(ctx,holder.edtCmnt)

      //  holder.txtPost.text = posts[position].postContent

        val adapterPrivacy = ArrayAdapter(ctx, R.layout.spin_setting1,privacyArray)
        adapterPrivacy.setDropDownViewResource(R.layout.spinner_txt)
        holder.spinPrivacy.adapter = adapterPrivacy
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
    }
}