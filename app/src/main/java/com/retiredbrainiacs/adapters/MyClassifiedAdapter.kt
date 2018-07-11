package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R
import com.retiredbrainiacs.common.Common
import kotlinx.android.synthetic.main.my_classified_adapter.view.*

class MyClassifiedAdapter(var ctx : Context): RecyclerView.Adapter<MyClassifiedAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var v = LayoutInflater.from(ctx).inflate(R.layout.my_classified_adapter,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Common.setFontRegular(ctx,holder.txtEdit)
        Common.setFontRegular(ctx,holder.txtDelete)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
val txtEdit = itemView.txt_edit
val txtDelete = itemView.txt_delete

    }
}