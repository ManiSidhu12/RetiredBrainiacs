package com.retiredbrainiacs.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.activities.ClassifiedDetail
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.all_classified_adapter.view.*

class AllClassifiedAdapter(var ctx : Context) : RecyclerView.Adapter<AllClassifiedAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.all_classified_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Common.setFontBold(ctx,holder.title)
        Common.setFontBold(ctx,holder.txtPost)
        Common.setFontRegular(ctx,holder.txtPostTime)
        Common.setFontRegular(ctx,holder.txtData)

        holder.layMain.setOnClickListener {
            ctx.startActivity(Intent(ctx,ClassifiedDetail::class.java))
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.txt_classifi_title
        val img = itemView.img_all_post
        val txtPost = itemView.txt_classifi_post_data
        val txtPostTime = itemView.txt_classifi_post_time
        val txtData = itemView.txt_classifi_all
        val layMain = itemView.lay_main_all_classify
    }
}