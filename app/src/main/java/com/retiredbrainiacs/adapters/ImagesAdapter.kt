package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.memorial.GalleryItemsQuery
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.images_adapter.view.*

class ImagesAdapter(var ctx: Context, var list : MutableList<GalleryItemsQuery>,var width : Int) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.images_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = holder.lay.getLayoutParams()
        params.width = width / 2
        holder.lay.setLayoutParams(params)
        if(list[position].image != null && !list[position].image.isEmpty()){
            Picasso.with(ctx).load(list[position].image).into(holder.img)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.image
        val lay = itemView.lay
    }
}