package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.archive.ModelDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.archive_detail_adap.view.*

class ArchiveDetailAdapter(var ctx: Context,var model: MutableList<ModelDetail>) : RecyclerView.Adapter<ArchiveDetailAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.archive_detail_adap,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(!model[position].file_note.isEmpty()){
            holder.txtContent.text = model[position].file_note
        }
        if (model[position].file_type.equals("pdf")){
        holder.imgDummy.setImageResource(R.drawable.pdf)
        }
        else   if (model[position].file_type.equals("mp3")){
            holder.imgDummy.setImageResource(R.drawable.mp3)
        }
        else if (model[position].file_type.equals("mp4")){
            holder.imgDummy.setImageResource(R.drawable.video)
        }
        else if (model[position].file_type.equals("doc")){
            holder.imgDummy.setImageResource(R.drawable.doc)
        }
        else if (model[position].file_type.equals("docx")){
            holder.imgDummy.setImageResource(R.drawable.doc)
        }
        else if (model[position].file_type.equals("images")){
          Picasso.with(ctx).load(model[position].file).into(holder.img)
        }
        else if (model[position].file_type.equals("youtube")){
            if(model[position].file != null && !model[position].file.isEmpty()){
                val u2 = model[position].file.split("/")
                Log.e("length", "" + u2)
                val u = u2[u2.size - 1]
                var u3: String = ""
                if(u.contains("=")){
                    u3= u.split("=")[1]
                }
                else{
                    u3 = u
                }
                val u1 = "http://img.youtube.com/vi/"+u3+"/0.jpg"
                Log.e("url",u1)
                Picasso.with(ctx).load(u1).into(holder.img)
            }

        }
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val txtContent = itemView.txt_content
        val imgDummy = itemView.audio_img
        val img = itemView.img_post
    }
}