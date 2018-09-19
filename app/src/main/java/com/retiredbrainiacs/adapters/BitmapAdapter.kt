package com.retiredbrainiacs.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.classified.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.images_adapter.view.*
import java.util.ArrayList

class BitmapAdapter(var ctx: Context, var listImages: ArrayList<Bitmap>, var width: Int, var type: String,var  images: MutableList<Image>) : RecyclerView.Adapter<BitmapAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.images_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        if(type.equals("bitmap")) {
            return listImages.size
        }
        else{
            return images.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = holder.lay.getLayoutParams()
        params.width = width / 3
        holder.lay.setLayoutParams(params)
        if(type.equals("bitmap")) {
            holder.btn.visibility = View.GONE

            holder.img.setImageBitmap(listImages[position])
        }
        else{
            holder.btn.visibility = View.VISIBLE
if(images[position].url != null && !images[position].url.isEmpty()){
    Picasso.with(ctx).load(images[position].url).into(holder.img)
}
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.image
        val btn = itemView.del
        val lay  = itemView.lay
    }
}