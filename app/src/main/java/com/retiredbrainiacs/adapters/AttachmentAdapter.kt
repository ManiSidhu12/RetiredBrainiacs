package com.retiredbrainiacs.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import kotlinx.android.synthetic.main.adapter_images.view.*

class AttachmentAdapter(var ctx: Context,var listImages: ArrayList<Bitmap>, var type: String) : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.adapter_images,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(type.equals("add")){
            holder.close.visibility = View.GONE
        }
        holder.img.setImageBitmap(listImages[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
val img = itemView.img
val close = itemView.img_close
    }
}