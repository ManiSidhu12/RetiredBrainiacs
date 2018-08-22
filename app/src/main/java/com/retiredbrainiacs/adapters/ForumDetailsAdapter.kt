package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.model.forum.FormMessage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.form_detail_adapter.view.*

class ForumDetailsAdapter(var ctx: Context, var formMain: MutableList<FormMessage>) : RecyclerView.Adapter<ForumDetailsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.form_detail_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return formMain.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = formMain[position].displayName
        holder.txtData.text = formMain[position].comment
        if(formMain[position].userImage != null && !formMain[position].userImage.isEmpty()){
            Picasso.with(ctx).load(formMain[position].userImage).into(holder.imgUser)
        }
        else{
            holder.imgUser.setImageResource(R.drawable.dummyuser)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val imgUser = itemView.img_user_forum
        val txtName = itemView.txt_name_forum
        val txtTime = itemView.txt_time_forum
        val txtData = itemView.txt_post_forum
    }
}