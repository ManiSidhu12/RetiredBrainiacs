package com.retiredbrainiacs.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.retiredbrainiacs.R

class ArchiveDetailAdapter(var ctx : Context) : RecyclerView.Adapter<ArchiveDetailAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(ctx).inflate(R.layout.form_detail_adapter,parent,false)
        return ViewHolder(v)    }

    override fun getItemCount(): Int {
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

    }
}