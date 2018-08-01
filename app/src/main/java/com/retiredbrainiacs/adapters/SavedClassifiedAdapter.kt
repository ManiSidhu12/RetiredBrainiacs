package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.classified.Classified
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pager_adap.view.*

class SavedClassifiedAdapter(var ctx: Context, var classified: MutableList<Classified>) : RecyclerView.Adapter<SavedClassifiedAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.pager_adap,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return classified.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = classified[position].title

        if(classified[position].image != null && !classified[position].image.isEmpty()){
            Picasso.with(ctx).load(classified[position].image).into(holder.img)
        }
        holder.txtTime.text = classified[position].postedOn
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
      val img = itemView.img_all_post
        val txtTitle = itemView.txt_classifi_post_data
        val txtTime = itemView.txt_classifi_post_time
    }
}