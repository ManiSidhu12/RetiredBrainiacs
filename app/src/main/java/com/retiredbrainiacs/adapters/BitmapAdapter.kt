package com.retiredbrainiacs.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import kotlinx.android.synthetic.main.images_adapter.view.*
import java.util.ArrayList

class BitmapAdapter(var ctx: Context, var listImages: ArrayList<Bitmap>,var width : Int) : RecyclerView.Adapter<BitmapAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.images_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = holder.lay.getLayoutParams()
        params.width = width / 2
        holder.lay.setLayoutParams(params)
        holder.btn.visibility = View.GONE
        holder.img.setImageBitmap(listImages[position])
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.image
        val btn = itemView.del
        val lay  = itemView.lay
    }
}